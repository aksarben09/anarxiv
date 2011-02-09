/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Copyright (C) 2011 Nephoapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.nephoapp.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Note: the original code directly accesses the protected members, mLeft, mRight, mScrollX and mScrollY.
 * 		 this is not OK in eclipse with ADT.
 */
public class Workspace extends ViewGroup 
{
	private static final int INVALID_SCREEN = -1;
	 
	/**
	 * The velocity at which a fling gesture will cause us to snap to the next screen
	 */
	private static final int SNAP_VELOCITY = 1000;

	private int mDefaultScreen;
	 
//	private final WallpaperManager mWallpaperManager;
	 
	private boolean mFirstLayout = true;

	private int mCurrentScreen;
	private int mNextScreen = INVALID_SCREEN;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	 
	/**
	 * CellInfo for the cell that is currently being dragged
	 */
//	private CellLayout.CellInfo mDragInfo;
	
	/**
     * Target drop area calculated during last acceptDrop call.
     */
//	private int[] mTargetCell = null;

	private float mLastMotionX;
	private float mLastMotionY;

	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;

	private int mTouchState = TOUCH_STATE_REST;

//	private OnLongClickListener mLongClickListener;
	private OnViewSwitchedListener mViewSwitchedListener = null;
	
//	private int mLeft = 0;
//	private int mRight = 0;
//	private int mScrollX = 0;
//	private int mScrollY = 0;

//	private Launcher mLauncher;
//	private DragController mDragger;
	
	/**
     * Cache of vacant cells, used during drag events and invalidated as needed.
     */
//	private CellLayout.CellInfo mVacantCache = null;
    
//	private int[] mTempCell = new int[2];
//	private int[] mTempEstimate = new int[2];

	private boolean mAllowLongPress;
	private boolean mLocked;

	private int mTouchSlop;
	private int mMaximumVelocity;

	final Rect mDrawerBounds = new Rect();
	final Rect mClipBounds = new Rect();
	int mDrawerContentHeight;
	int mDrawerContentWidth;
	
	/**
	 * 
	 * @param context
	 */
	public Workspace(Context context)
	{
		this(context, null);
	}
	    
	/**
	 * Used to inflate the Workspace from XML.
	 *
	 * @param context The application's context.
	 * @param attrs The attribtues set containing the Workspace's customization values.
	 */
	public Workspace(Context context, AttributeSet attrs) 
	{
		this(context, attrs, 0);
	}

	/**
	 * Used to inflate the Workspace from XML.
	 *
	 * @param context The application's context.
	 * @param attrs The attribtues set containing the Workspace's customization values.
	 * @param defStyle Unused.
	 */
	public Workspace(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);

//		mWallpaperManager = WallpaperManager.getInstance(context);

//		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Workspace, defStyle, 0);
		mDefaultScreen = /*a.getInt(R.styleable.Workspace_defaultScreen, 1)*/0;
//		a.recycle();

		initWorkspace();
	}
	
	/**
	 * Initializes various states for this workspace.
	 */
	private void initWorkspace() 
	{
		mScroller = new Scroller(getContext());
		mCurrentScreen = mDefaultScreen;
//		Launcher.setScreen(mCurrentScreen);

		final ViewConfiguration configuration = ViewConfiguration.get(getContext());
		mTouchSlop = configuration.getScaledTouchSlop();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
	}
	
	boolean isDefaultScreenShowing()
	{
		return mCurrentScreen == mDefaultScreen;
	}
	
	/**
	 * Returns the index of the currently displayed screen.
	 *
	 * @return The index of the currently displayed screen.
	 */
	int getCurrentScreen()
	{
		return mCurrentScreen;
	}
	
	/**
	 * Sets the current screen.
	 *
	 * @param currentScreen
	 */
	void setCurrentScreen(int currentScreen)
	{
//		clearVacantCache();
		mCurrentScreen = Math.max(0, Math.min(currentScreen, getChildCount() - 1));
		scrollTo(mCurrentScreen * getWidth(), 0);
		invalidate();
	}
	
	/**
	 * set the current screen using the view tag.
	 * 
	 * @author ritsu
	 * @param tag
	 */
	public void setCurrentScreen(Object tag)
	{
		int screen = getScreenForView(getViewForTag(tag));
		mCurrentScreen = Math.max(0, Math.min(screen, getChildCount() - 1));
		scrollTo(mCurrentScreen * getWidth(), 0);

		/* call listener. */
		if (mViewSwitchedListener != null)
		{
			View v = getChildAt(screen);
			mViewSwitchedListener.onViewSwitched(v, v.getTag(), screen);
		}
	}
	
	/**
	 * Registers the specified listener on each screen contained in this workspace.
	 *
	 * @param l The listener used to respond to long clicks.
	 */
	@Override
	public void setOnLongClickListener(OnLongClickListener l)
	{
//		mLongClickListener = l;
		final int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			getChildAt(i).setOnLongClickListener(l);
		}
	}
	
	/**
	 * @author ritsu
	 * @param l
	 */
	public void setOnViewSwitchedListener(OnViewSwitchedListener l)
	{
		mViewSwitchedListener = l;
		
		/* call it immediately to notify the observer. */
		if (mViewSwitchedListener != null)
		{
			View v = getChildAt(mCurrentScreen);
			mViewSwitchedListener.onViewSwitched(v, v.getTag(), mCurrentScreen);
		}
	}
	
	private void updateWallpaperOffset() 
	{
		updateWallpaperOffset(getChildAt(getChildCount() - 1).getRight() - (getRight() - getLeft()));
	}

	private void updateWallpaperOffset(int scrollRange)
	{
//		mWallpaperManager.setWallpaperOffsetSteps(1.0f / (getChildCount() - 1), 0 );
//		mWallpaperManager.setWallpaperOffsets(getWindowToken(), getScrollX() / (float) scrollRange, 0);
	}
	
	@Override
	public void computeScroll() 
	{
		if (mScroller.computeScrollOffset()) 
		{
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			updateWallpaperOffset();
			postInvalidate();
		} 
		else if (mNextScreen != INVALID_SCREEN)
		{
			mCurrentScreen = Math.max(0, Math.min(mNextScreen, getChildCount() - 1));
//			Launcher.setScreen(mCurrentScreen);
			mNextScreen = INVALID_SCREEN;
//			clearChildrenCache();
		}
	}
    
	@Override
	public boolean isOpaque()
	{
	 	return false;
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		boolean restore = false;

		// ViewGroup.dispatchDraw() supports many features we don't need:
		// clip to padding, layout animation, animation listener, disappearing
		// children, etc. The following implementation attempts to fast-track
		// the drawing dispatch by drawing only what we know needs to be drawn.

		boolean fastDraw = mTouchState != TOUCH_STATE_SCROLLING && mNextScreen == INVALID_SCREEN;
		// If we are not scrolling or flinging, draw only the current screen
		if (fastDraw)
		{
			drawChild(canvas, getChildAt(mCurrentScreen), getDrawingTime());
		} 
		else
		{
			final long drawingTime = getDrawingTime();
			// If we are flinging, draw only the current screen and the target screen
			if (mNextScreen >= 0 && mNextScreen < getChildCount() &&
				Math.abs(mCurrentScreen - mNextScreen) == 1)
			{
				drawChild(canvas, getChildAt(mCurrentScreen), drawingTime);
				drawChild(canvas, getChildAt(mNextScreen), drawingTime);
			}
			else
			{
				// If we are scrolling, draw all of our children
				final int count = getChildCount();
				for (int i = 0; i < count; i++) 
				{
					drawChild(canvas, getChildAt(i), drawingTime);
				}
			}
		}

		if (restore)
		{
			canvas.restore();
		}
    }
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) 
		{
			throw new IllegalStateException("Workspace can only be used in EXACTLY mode.");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY)
		{
			throw new IllegalStateException("Workspace can only be used in EXACTLY mode.");
		}

		// The children are given the same width and height as the workspace
		final int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		if (mFirstLayout)
		{
			scrollTo(mCurrentScreen * width, 0);
			updateWallpaperOffset(width * (getChildCount() - 1));
			mFirstLayout = false;
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) 
	{
		int childLeft = 0;

		final int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE)
			{
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth, child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	@Override
	public boolean dispatchUnhandledMove(View focused, int direction)
	{
		if (direction == View.FOCUS_LEFT)
		{
			if (getCurrentScreen() > 0) 
			{
				snapToScreen(getCurrentScreen() - 1);
				return true;
			}
		}
		else if (direction == View.FOCUS_RIGHT)
		{
			if (getCurrentScreen() < getChildCount() - 1)
			{
				snapToScreen(getCurrentScreen() + 1);
				return true;
			}
		}
		return super.dispatchUnhandledMove(focused, direction);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		if (mLocked /*|| !mLauncher.isDrawerDown()*/) 
		{
			return true;
		}

        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called and we do the actual
         * scrolling there.
         */

        /*
         * Shortcut the most recurring case: the user is in the dragging
         * state and he is moving his finger.  We want to intercept this
         * motion.
         */
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST))
		{
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action)
		{
			case MotionEvent.ACTION_MOVE:
				/*
				 * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
				 * whether the user has moved far enough from his original down touch.
				 */

				/*
				 * Locally do absolute value. mLastMotionX is set to the y value
				 * of the down event.
				 */
				final int xDiff = (int) Math.abs(x - mLastMotionX);
				final int yDiff = (int) Math.abs(y - mLastMotionY);

				final int touchSlop = mTouchSlop;
				boolean xMoved = xDiff > touchSlop;
				boolean yMoved = yDiff > touchSlop;

				if (xMoved || yMoved)
				{
                    
					if (xMoved)
					{
						// Scroll if the user moved far enough along the X axis
						mTouchState = TOUCH_STATE_SCROLLING;
//						enableChildrenCache();
					}
					// Either way, cancel any pending longpress
					if (mAllowLongPress)
					{
						mAllowLongPress = false;
						// Try canceling the long press. It could also have been scheduled
						// by a distant descendant, so use the mAllowLongPress flag to block
						// everything
						final View currentScreen = getChildAt(mCurrentScreen);
						currentScreen.cancelLongPress();
					}
				}
				break;

			case MotionEvent.ACTION_DOWN:
				// Remember location of down touch
				mLastMotionX = x;
				mLastMotionY = y;
				mAllowLongPress = true;

				/*
				 * If being flinged and user touches the screen, initiate drag;
				 * otherwise don't.  mScroller.isFinished should be false when
				 * being flinged.
				 */
				mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
				break;

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:

//				if (mTouchState != TOUCH_STATE_SCROLLING)
//				{
//					final CellLayout currentScreen = (CellLayout) getChildAt(mCurrentScreen);
//					if (!currentScreen.lastDownOnOccupiedCell())
//					{
//						getLocationOnScreen(mTempCell);
						// Send a tap to the wallpaper if the last down was on empty space
//						mWallpaperManager.sendWallpaperCommand(getWindowToken(), 
//								"android.wallpaper.tap",
//								mTempCell[0] + (int) ev.getX(),
//								mTempCell[1] + (int) ev.getY(), 0, null);
//					}
//				}

				// Release the drag
//				clearChildrenCache();
				mTouchState = TOUCH_STATE_REST;
				mAllowLongPress = false;
				break;
        }

        /*
         * The only time we want to intercept motion events is if we are in the
         * drag mode.
         */
        return mTouchState != TOUCH_STATE_REST;
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (mLocked /*|| !mLauncher.isDrawerDown()*/)
		{
			return true;
		}

		if (mVelocityTracker == null)
		{
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();

		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
			if (!mScroller.isFinished())
			{
				mScroller.abortAnimation();
			}

			// Remember where the motion event started
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mTouchState == TOUCH_STATE_SCROLLING)
			{
				// Scroll to follow the motion event
				final int deltaX = (int) (mLastMotionX - x);
				mLastMotionX = x;

				if (deltaX < 0)
				{
					if (getScrollX() > 0) 
					{
						scrollBy(Math.max(-getScrollX(), deltaX), 0);
						updateWallpaperOffset();
					}
				}
				else if (deltaX > 0)
				{
					final int availableToScroll = getChildAt(getChildCount() - 1).getRight() -
													getScrollX() - getWidth();
					if (availableToScroll > 0)
					{
						scrollBy(Math.min(availableToScroll, deltaX), 0);
						updateWallpaperOffset();
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_SCROLLING)
			{
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				int velocityX = (int) velocityTracker.getXVelocity();

				if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0)
				{
					// Fling hard enough to move left
					snapToScreen(mCurrentScreen - 1);
				}
				else if
				(velocityX < -SNAP_VELOCITY && mCurrentScreen < getChildCount() - 1)
				{
					// Fling hard enough to move right
					snapToScreen(mCurrentScreen + 1);
				}
				else
				{
					snapToDestination();
				}

				if (mVelocityTracker != null)
				{
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
		}

		return true;
	}
	
	private void snapToDestination()
	{
		final int screenWidth = getWidth();
		final int whichScreen = (getScrollX() + (screenWidth / 2)) / screenWidth;

		snapToScreen(whichScreen);
	}
	
	void snapToScreen(int whichScreen)
	{
		if (!mScroller.isFinished())
			return;

//		clearVacantCache();
//		enableChildrenCache();

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		boolean changingScreens = whichScreen != mCurrentScreen;
        
		mNextScreen = whichScreen;
        
		View focusedChild = getFocusedChild();
		if (focusedChild != null && changingScreens && focusedChild == getChildAt(mCurrentScreen))
		{
			focusedChild.clearFocus();
		}

		final int newX = whichScreen * getWidth();
		final int delta = newX - getScrollX();
		mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
		invalidate();
		
		/* call listener. */
		if (mViewSwitchedListener != null)
		{
			View v = getChildAt(whichScreen);
			mViewSwitchedListener.onViewSwitched(v, v.getTag(), whichScreen);
		}
	}
	
	@Override
	protected Parcelable onSaveInstanceState()
	{
		final SavedState state = new SavedState(super.onSaveInstanceState());
		state.currentScreen = mCurrentScreen;
		return state;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state)
	{
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		if (savedState.currentScreen != -1)
		{
			mCurrentScreen = savedState.currentScreen;
//			Launcher.setScreen(mCurrentScreen);
		}
	}
	
	public void scrollLeft()
	{
//		clearVacantCache();
		if (mNextScreen == INVALID_SCREEN && mCurrentScreen > 0 && mScroller.isFinished())
		{
			snapToScreen(mCurrentScreen - 1);
		}
	}
	
	public void scrollRight()
	{
//		clearVacantCache();
		if (mNextScreen == INVALID_SCREEN && mCurrentScreen < getChildCount() -1 &&
				mScroller.isFinished())
		{
			snapToScreen(mCurrentScreen + 1);
		}
	}
	
	public int getScreenForView(View v)
	{
		int result = -1;
		if (v != null)
		{
//			ViewParent vp = v.getParent();
			int count = getChildCount();
			for (int i = 0; i < count; i++)
			{
				if (/*vp*/v == getChildAt(i))
				{
					return i;
				}
			}
		}
		return result;
	}
	
	public View getViewForTag(Object tag)
	{
		int screenCount = getChildCount();
		for (int screen = 0; screen < screenCount; screen++)
		{
			View child = getChildAt(screen);
			if (child.getTag() == tag)
			{
				return child;
			}
		}
		return null;
	}
	
	/**
	 * Unlocks the SlidingDrawer so that touch events are processed.
	 *
	 * @see #lock()
	 */
	public void unlock()
	{
		mLocked = false;
	}
	
	/**
	 * Locks the SlidingDrawer so that touch events are ignores.
	 *
	 * @see #unlock()
	 */
	public void lock()
	{
		mLocked = true;
	}
	
	/**
	 * @return True is long presses are still allowed for the current touch
	 */
	public boolean allowLongPress()
	{
		return mAllowLongPress;
	}
    
	/**
	 * Set true to allow long-press events to be triggered, usually checked by
	 * {@link Launcher} to accept or block dpad-initiated long-presses.
	 */
	public void setAllowLongPress(boolean allowLongPress)
	{
		mAllowLongPress = allowLongPress;
	}
	
	void moveToDefaultScreen()
	{
		snapToScreen(mDefaultScreen);
		getChildAt(mDefaultScreen).requestFocus();
	}
	
	/**
	 *
	 *
	 */
	public static class SavedState extends BaseSavedState
	{
		int currentScreen = -1;

		SavedState(Parcelable superState)
		{
			super(superState);
		}

		private SavedState(Parcel in)
		{
			super(in);
			currentScreen = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel out, int flags)
		{
			super.writeToParcel(out, flags);
			out.writeInt(currentScreen);
		}

		public static final Parcelable.Creator<SavedState> CREATOR =
				new Parcelable.Creator<SavedState>() 
				{
					public SavedState createFromParcel(Parcel in)
					{
						return new SavedState(in);
					}
					
					public SavedState[] newArray(int size)
					{
						return new SavedState[size];
					}
				};
	}
	
	/**
	 *
	 * @author ritsu
	 */
	public static interface OnViewSwitchedListener
	{
		public abstract void onViewSwitched(View v, Object tag, int index);
	}
}
