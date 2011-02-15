/*
 * Copyright (C) 2011 Nephoapp
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
 * This class is adapted from android source code by combining TabHost and TabWidget.
 */

package com.nephoapp.ui;

import java.util.ArrayList;
import java.util.List;

import com.nephoapp.anarxiv.R;

import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

/**
 * This is a TabContainer without views.
 * Only tabs are displayed.
 */
public class TabContainer extends LinearLayout implements View.OnFocusChangeListener
{
	private List<TabSpec> mTabSpecs = new ArrayList<TabSpec>(2);
	/**
	 * This field should be made private, so it is hidden from the SDK.
	 * {@hide}
	 */
	protected int mCurrentTab = -1;
	/**
	 * This field should be made private, so it is hidden from the SDK.
	 * {@hide}
	 */
	private OnTabChangeListener mOnTabChangeListener;
	private OnKeyListener mTabKeyListener;
	
	private OnTabSelectionChanged mSelectionChangedListener;
	private int mSelectedTab = 0;
	private Drawable mBottomLeftStrip;
	private Drawable mBottomRightStrip;
	private boolean mStripMoved;
	private Drawable mDividerDrawable;
	private boolean mDrawBottomStrips = true;
    
	/**
	 * 
	 * @param context
	 */
	public TabContainer(Context context)
	{
		this(context, null);
	}
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public TabContainer(Context context, AttributeSet attrs)
	{
		this(context, attrs, android.R.attr.tabWidgetStyle);
	}
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public TabContainer(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs);
		initTabContainer();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		mStripMoved = true;
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected int getChildDrawingOrder(int childCount, int i)
	{
		// Always draw the selected tab last, so that drop shadows are drawn
		// in the correct z-order.
		if (i == childCount - 1)
		{
			return mSelectedTab;
		}
		else if (i >= mSelectedTab)
		{
			return i + 1;
		}
		else
		{
			return i;
		}
	}
	
	private void initTabWidget()
	{
		setOrientation(LinearLayout.HORIZONTAL);
		this.setChildrenDrawingOrderEnabled(true);

		final Context context = getContext();
		final Resources resources = context.getResources();
        
		// Use modern color scheme for Eclair and beyond
		mBottomLeftStrip = resources.getDrawable(
										R.drawable.tab_bottom_left);
		mBottomRightStrip = resources.getDrawable(
										R.drawable.tab_bottom_right); 

		// Deal with focus, as we don't want the focus to go by default
		// to a tab other than the current tab
		setFocusable(true);
		setOnFocusChangeListener(this);
	}

	/**
	 * Returns the tab indicator view at the given index.
	 *
	 * @param index the zero-based index of the tab indicator view to return
	 * @return the tab indicator view at the given index
	 */
	public View getChildTabViewAt(int index)
	{
		// If we are using dividers, then instead of tab views at 0, 1, 2, ...
		// we have tab views at 0, 2, 4, ...
		if (mDividerDrawable != null)
		{
			index *= 2;
		}
		return getChildAt(index);
	}

	/**
	 * Returns the number of tab indicator views.
	 * @return the number of tab indicator views.
	 */
	public int getTabCount()
	{
		int children = getChildCount();

		// If we have dividers, then we will always have an odd number of
		// children: 1, 3, 5, ... and we want to convert that sequence to
		// this: 1, 2, 3, ...
		if (mDividerDrawable != null)
		{
			children = (children + 1) / 2;
		}
		return children;
	}

	/**
	 * Sets the drawable to use as a divider between the tab indicators.
	 * @param drawable the divider drawable
	 */
	public void setDividerDrawable(Drawable drawable)
	{
		mDividerDrawable = drawable;
	}
	
	/**
	 * Sets the drawable to use as a divider between the tab indicators.
	 * @param resId the resource identifier of the drawable to use as a
	 * divider.
	 */
	public void setDividerDrawable(int resId)
	{
		mDividerDrawable = getContext().getResources().getDrawable(resId);
	}
	
	/**
	 * Controls whether the bottom strips on the tab indicators are drawn or
	 * not.  The default is to draw them.  If the user specifies a custom
	 * view for the tab indicators, then the TabHost class calls this method
	 * to disable drawing of the bottom strips.
	 * @param drawBottomStrips true if the bottom strips should be drawn.
	 */
	void setDrawBottomStrips(boolean drawBottomStrips)
	{
		mDrawBottomStrips = drawBottomStrips;
	}
	
	@Override
	public void childDrawableStateChanged(View child)
	{
		if (child == getChildTabViewAt(mSelectedTab))
		{
			// To make sure that the bottom strip is redrawn
			invalidate();
		}
		super.childDrawableStateChanged(child);
	}
	
	@Override
	public void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);

		// If the user specified a custom view for the tab indicators, then
		// do not draw the bottom strips.
		if (!mDrawBottomStrips)
		{
			// Skip drawing the bottom strips.
			return;
		}

		View selectedChild = getChildTabViewAt(mSelectedTab);

		mBottomLeftStrip.setState(selectedChild.getDrawableState());
		mBottomRightStrip.setState(selectedChild.getDrawableState());

		if (mStripMoved)
		{
			Rect selBounds = new Rect(); // Bounds of the selected tab indicator
			selBounds.left = selectedChild.getLeft();
			selBounds.right = selectedChild.getRight();
			final int myHeight = getHeight();
			mBottomLeftStrip.setBounds(
					Math.min(0, selBounds.left
								- mBottomLeftStrip.getIntrinsicWidth()),
							 myHeight - mBottomLeftStrip.getIntrinsicHeight(),
							 selBounds.left,
							 getHeight());
			mBottomRightStrip.setBounds(
					selBounds.right,
					myHeight - mBottomRightStrip.getIntrinsicHeight(),
					Math.max(getWidth(),
							 selBounds.right + mBottomRightStrip.getIntrinsicWidth()),
							 myHeight);
			mStripMoved = false;
		}

		mBottomLeftStrip.draw(canvas);
		mBottomRightStrip.draw(canvas);
	}
	
	/**
	 * Sets the current tab.
	 */
	public void TabWidget_setCurrentTab(int index)
	{
		if (index < 0 || index >= getTabCount())
		{
			return;
		}

		getChildTabViewAt(mSelectedTab).setSelected(false);
		mSelectedTab = index;
		getChildTabViewAt(mSelectedTab).setSelected(true);
		mStripMoved = true;
	}
	
	/**
	 * Sets the current tab and focuses the UI on it.
	 */
	public void focusCurrentTab(int index)
	{
		final int oldTab = mSelectedTab;

		// set the tab
		TabWidget_setCurrentTab(index);

		// change the focus if applicable.
		if (oldTab != index)
		{
			getChildTabViewAt(index).requestFocus();
		}
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		int count = getTabCount();

		for (int i = 0; i < count; i++)
		{
			View child = getChildTabViewAt(i);
			child.setEnabled(enabled);
		}
	}
	
	@Override
	public void addView(View child)
	{
		if (child.getLayoutParams() == null)
		{
			final LinearLayout.LayoutParams lp = new LayoutParams(
					0,
					ViewGroup.LayoutParams.FILL_PARENT, 1.0f);
			lp.setMargins(0, 0, 0, 0);
			child.setLayoutParams(lp);
		}

		// Ensure you can navigate to the tab with the keyboard, and you can touch it
		child.setFocusable(true);
		child.setClickable(true);

		// If we have dividers between the tabs and we already have at least one
		// tab, then add a divider before adding the next tab.
		if (mDividerDrawable != null && getTabCount() > 0)
		{
			ImageView divider = new ImageView(getContext());
			final LinearLayout.LayoutParams lp = new LayoutParams(
					mDividerDrawable.getIntrinsicWidth(),
					LayoutParams.FILL_PARENT);
			lp.setMargins(0, 0, 0, 0);
			divider.setLayoutParams(lp);
			divider.setBackgroundDrawable(mDividerDrawable);
			super.addView(divider);
		}
		super.addView(child);

		// TODO: detect this via geometry with a tabwidget listener rather
		// than potentially interfere with the view's listener
		child.setOnClickListener(new TabClickListener(getTabCount() - 1));
		child.setOnFocusChangeListener(this);
    }
	
	/**
	 * Provides a way for {@link TabHost} to be notified that the user clicked on a tab indicator.
	 */
	void setTabSelectionListener(OnTabSelectionChanged listener)
	{
		mSelectionChangedListener = listener;
	}
	
	public void onFocusChange(View v, boolean hasFocus)
	{
		if (v == this && hasFocus)
		{
			getChildTabViewAt(mSelectedTab).requestFocus();
			return;
		}

		if (hasFocus)
		{
			int i = 0;
			int numTabs = getTabCount();
			while (i < numTabs)
			{
				if (getChildTabViewAt(i) == v)
				{
					setCurrentTab(i);
					mSelectionChangedListener.onTabSelectionChanged(i, false);
					break;
				}
				i++;
			}
		}
	}
	
	// registered with each tab indicator so we can notify tab host
	private class TabClickListener implements OnClickListener
	{
		private final int mTabIndex;

		private TabClickListener(int tabIndex)
		{
			mTabIndex = tabIndex;
		}

		public void onClick(View v)
		{
			mSelectionChangedListener.onTabSelectionChanged(mTabIndex, true);
		}
    }
	
	/**
	 * 
	 */
	public void setup()
	{
		mTabKeyListener = new OnKeyListener()
		{
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				switch (keyCode)
				{
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_DPAD_LEFT:
					case KeyEvent.KEYCODE_DPAD_RIGHT:
					case KeyEvent.KEYCODE_DPAD_UP:
					case KeyEvent.KEYCODE_DPAD_DOWN:
					case KeyEvent.KEYCODE_ENTER:
						return false;
				}

				return false;
			}

		};
		
		setTabSelectionListener(new OnTabSelectionChanged()
			{
				public void onTabSelectionChanged(int tabIndex, boolean clicked)
				{
					setCurrentTab(tabIndex);
					if (clicked)
					{
					}
				}
			});
	}
	
	private void initTabContainer() 
	{
		initTabWidget();
		setFocusableInTouchMode(true);
		setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

		mCurrentTab = -1;
    }
	
	/**
	 * Get a new {@link TabSpec} associated with this tab host.
	 * @param tag required tag of tab.
	 */
	public TabSpec newTabSpec(String tag) 
	{
		return new TabSpec(tag);
	}
	
	/**
	 * Add a tab.
	 * @param tabSpec Specifies how to create the indicator and content.
	 */
	public void addTab(TabSpec tabSpec)
	{

		if (tabSpec.mIndicatorStrategy == null) 
		{
			throw new IllegalArgumentException("you must specify a way to create the tab indicator.");
		}

		View tabIndicator = tabSpec.mIndicatorStrategy.createIndicatorView();
		tabIndicator.setOnKeyListener(mTabKeyListener);

		// If this is a custom view, then do not draw the bottom strips for
		// the tab indicators.
		this.addView(tabIndicator);
		mTabSpecs.add(tabSpec);

		if (mCurrentTab == -1)
		{
			setCurrentTab(0);
		}
	}
	
	/**
	 * Removes all tabs from the tab widget associated with this tab host.
	*/
	public void clearAllTabs()
	{
		removeAllViews();
		initTabContainer();
		mTabSpecs.clear();
		requestLayout();
		invalidate();
    }
	
	public int getCurrentTab()
	{
		return mCurrentTab;
	}

	public String getCurrentTabTag()
	{
		if (mCurrentTab >= 0 && mCurrentTab < mTabSpecs.size()) 
		{
			return mTabSpecs.get(mCurrentTab).getTag();
		}
		return null;
	}

	public View getCurrentTabView()
	{
		if (mCurrentTab >= 0 && mCurrentTab < mTabSpecs.size())
		{
			return getChildTabViewAt(mCurrentTab);
		}
		return null;
	}
	
	public void setCurrentTabByTag(String tag)
	{
		int i;
		for (i = 0; i < mTabSpecs.size(); i++) 
		{
			if (mTabSpecs.get(i).getTag().equals(tag))
			{
				setCurrentTab(i);
				break;
			}
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		final boolean handled = super.dispatchKeyEvent(event);

		// unhandled key ups change focus to tab indicator for embedded activities
		// when there is nothing that will take focus from default focus searching
		if (!handled
				&& (event.getAction() == KeyEvent.ACTION_DOWN)
				&& (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP)
			)
		{
			getChildTabViewAt(mCurrentTab).requestFocus();
			playSoundEffect(SoundEffectConstants.NAVIGATION_UP);
			return true;
		}
		return handled;
	}
	
	public void setCurrentTab(int index) 
	{
		if (index < 0 || index >= mTabSpecs.size())
		{
			return;
		}

		if (index == mCurrentTab)
		{
			return;
		}

		mCurrentTab = index;

		// Call the tab widget's focusCurrentTab(), instead of just
		// selecting the tab.
		this.focusCurrentTab(mCurrentTab);

		invokeOnTabChangeListener();
    }
	
	/**
	 * Register a callback to be invoked when the selected state of any of the items
	 * in this list changes
	 * @param l
	 * The callback that will run
	 */
	public void setOnTabChangedListener(OnTabChangeListener l)
	{
		mOnTabChangeListener = l;
	}

	private void invokeOnTabChangeListener()
	{
		if (mOnTabChangeListener != null)
		{
			mOnTabChangeListener.onTabChanged(getCurrentTabTag());
		}
	}
	
	/**
	 * Interface definition for a callback to be invoked when tab changed
	 */
	public interface OnTabChangeListener
	{
		void onTabChanged(String tabId);
	}
	
	/**
	 * Makes the content of a tab when it is selected. Use this if your tab
	 * content needs to be created on demand, i.e. you are not showing an
	 * existing view or starting an activity.
	 */
	public interface TabContentFactory 
	{
		/**
		 * Callback to make the tab contents
		 *
		 * @param tag
		 *            Which tab was selected.
		 * @return The view to display the contents of the selected tab.
		 */
		View createTabContent(String tag);
	}

	/**
	 * A tab has a tab indicator, content, and a tag that is used to keep
	 * track of it.  This builder helps choose among these options.
	 *
	 * For the tab indicator, your choices are:
	 * 1) set a label
	 * 2) set a label and an icon
	 *
	 * For the tab content, your choices are:
	 * 1) the id of a {@link View}
	 * 2) a {@link TabContentFactory} that creates the {@link View} content.
	 * 3) an {@link Intent} that launches an {@link android.app.Activity}.
	 */
	public class TabSpec
	{
		private String mTag;

		private IndicatorStrategy mIndicatorStrategy;

		private TabSpec(String tag)
		{
			mTag = tag;
		}

		/**
		 * Specify a label as the tab indicator.
		 */
		public TabSpec setIndicator(CharSequence label)
		{
			mIndicatorStrategy = new LabelIndicatorStrategy(label);
			return this;
		}

		/**
		 * Specify a label and icon as the tab indicator.
		 */
		public TabSpec setIndicator(CharSequence label, Drawable icon)
		{
			mIndicatorStrategy = new LabelAndIconIndicatorStrategy(label, icon);
			return this;
		}

		public String getTag()
		{
			return mTag;
		}
	}
	
	/**
	 * Specifies what you do to create a tab indicator.
	 */
	private static interface IndicatorStrategy
	{
		/**
		 * Return the view for the indicator.
		 */
		View createIndicatorView();
	}
	
	/**
	 * How to create a tab indicator that just has a label.
	 */
	private class LabelIndicatorStrategy implements IndicatorStrategy
	{

		private final CharSequence mLabel;

		private LabelIndicatorStrategy(CharSequence label)
		{
			mLabel = label;
		}

		public View createIndicatorView() 
		{
			final Context context = getContext();
			LayoutInflater inflater =
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View tabIndicator = inflater.inflate(R.layout.tab_indicator,
					TabContainer.this, // tab widget is the parent
					false); // no inflate params

			final TextView tv = (TextView) tabIndicator.findViewById(R.id.title);
			tv.setText(mLabel);
            
			return tabIndicator;
		}
	}

	/**
	 * How we create a tab indicator that has a label and an icon
	 */
	private class LabelAndIconIndicatorStrategy implements IndicatorStrategy
	{

		private final CharSequence mLabel;
		private final Drawable mIcon;

		private LabelAndIconIndicatorStrategy(CharSequence label, Drawable icon)
		{
			mLabel = label;
			mIcon = icon;
		}

		public View createIndicatorView()
		{
			final Context context = getContext();
			LayoutInflater inflater =
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View tabIndicator = inflater.inflate(R.layout.tab_indicator,
					TabContainer.this, // tab widget is the parent
					false); // no inflate params

			final TextView tv = (TextView) tabIndicator.findViewById(R.id.title);
			tv.setText(mLabel);

			final ImageView iconView = (ImageView) tabIndicator.findViewById(R.id.icon);
			iconView.setImageDrawable(mIcon);

			return tabIndicator;
		}
	}
    
	/**
	 * Let {@link TabHost} know that the user clicked on a tab indicator.
	 */
	static interface OnTabSelectionChanged
	{
		void onTabSelectionChanged(int tabIndex, boolean clicked);
    }
}
