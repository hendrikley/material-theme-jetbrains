/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Chris Magnussen and Elior Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.chrisrm.idea.lafs;

import com.chrisrm.idea.MTConfig;
import com.chrisrm.idea.themes.models.MTThemeable;
import com.chrisrm.idea.ui.*;
import com.chrisrm.idea.ui.indicators.MTSelectedTreePainter;
import com.intellij.icons.AllIcons;
import com.intellij.ide.ui.laf.darcula.ui.DarculaMenuBarBorder;
import com.intellij.ide.ui.laf.darcula.ui.DarculaMenuItemBorder;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.ui.components.JBScrollBar;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Service to install Material Theme properties in the UIManager
 *
 * @author helio
 * Created on 2018-10-29
 */
@SuppressWarnings( {"ClassWithTooManyMethods",
    "OverlyLongMethod",
    "DuplicateStringLiteralInspection",
    "OverlyCoupledClass"})
public
class MTLafInstaller {
  /**
   * The configuration
   */
  private final MTConfig mtConfig;
  /**
   * The Theme
   */
  @Nullable
  private final MTThemeable theme;

  /**
   * Constructor MTLafInstaller creates a new MTLafInstaller instance.
   */
  public MTLafInstaller() {
    mtConfig = MTConfig.getInstance();
    theme = null;
  }

  /**
   * Constructor MTLafInstaller creates a new MTLafInstaller instance.
   *
   * @param theme of type MTThemeable
   */
  MTLafInstaller(@Nullable final MTThemeable theme) {
    mtConfig = MTConfig.getInstance();
    this.theme = theme;
  }

  /**
   * Install Material Theme UI Components.
   * <p>
   * Some components will only be installed if the Material Components option is set, while others depend on other options, such as
   * Compact Statusbars, Arrow Styles, Title bar and so on.
   *
   * @param defaults the UIManager defaults to install properties into
   */
  public final void installMTDefaults(final UIDefaults defaults) {
    replaceStatusBar(defaults);
    replaceTree(defaults);
    replaceSelectedIndicator(defaults);
    replaceDropdowns(defaults);
    replaceTableHeaders(defaults);
    replaceIcons(defaults);
    replaceRootPane(defaults);
    replaceMenus(defaults);
    replaceTabbedPanes(defaults);

    if (mtConfig.isMaterialDesign()) {
      replaceButtons(defaults);
      replaceTextFields(defaults);
      replaceProgressBar(defaults);
      replaceTables(defaults);
      replaceSpinners(defaults);
      replaceCheckboxes(defaults);
      replaceRadioButtons(defaults);
      replaceSliders(defaults);
      replaceTextAreas(defaults);
      modifyRegistry();
    }
  }

  /**
   * Install non themeable defaults, such as borders, insets and so on
   *
   * @param defaults of type UIDefaults
   */
  @SuppressWarnings("DuplicateStringLiteralInspection")
  final void installDefaults(@NonNls final UIDefaults defaults) {
    defaults.put("Caret.width", 2);
    defaults.put("Border.width", 2);
    defaults.put("CellEditor.border.width", 2);

    defaults.put("Button.arc", 6);
    defaults.put("Component.arc", 0);

    defaults.put("Menu.maxGutterIconWidth", 18);
    defaults.put("MenuItem.maxGutterIconWidth", 18);
    defaults.put("MenuItem.acceleratorDelimiter", "-");
    defaults.put("MenuItem.border", new DarculaMenuItemBorder());
    defaults.put("Menu.border", new DarculaMenuItemBorder());
    defaults.put("MenuBar.border", new DarculaMenuBarBorder());

    defaults.put("TextArea.caretBlinkRate", 500);
    defaults.put("Table.cellNoFocusBorder", JBUI.insets(4, 4, 4, 4));
    defaults.put("CheckBoxMenuItem.borderPainted", false);
    defaults.put("RadioButtonMenuItem.borderPainted", false);
    defaults.put("ComboBox.squareButton", true);
    defaults.put("ComboBox.padding", JBUI.insets(1, 5, 1, 5));
    defaults.put("CheckBox.border.width", 3);
    defaults.put("RadioButton.border.width", 3);

    defaults.put("HelpTooltip.verticalGap", 4);
    defaults.put("HelpTooltip.horizontalGap", 10);
    defaults.put("HelpTooltip.maxWidth", 250);
    defaults.put("HelpTooltip.xOffset", 1);
    defaults.put("HelpTooltip.yOffset", 1);

    defaults.put("HelpTooltip.defaultTextBorderInsets", JBUI.insets(10, 10, 10, 16));
    defaults.put("HelpTooltip.fontSizeDelta", 0);
    defaults.put("HelpTooltip.smallTextBorderInsets", JBUI.insets(4, 8, 5, 8));
    defaults.put("ValidationTooltip.maxWidth", 384);

    defaults.put("Spinner.arrowButtonInsets", JBUI.insets(1, 1, 1, 1));
    defaults.put("Spinner.editorBorderPainted", false);
    defaults.put("ScrollBarUI", JBScrollBar.class.getName());
    defaults.put(JBScrollBar.class.getName(), JBScrollBar.class);

    defaults.put("Focus.activeErrorBorderColor", new ColorUIResource(0xE53935));
    defaults.put("Component.focusErrorColor", new ColorUIResource(0xE53935));
    defaults.put("Focus.inactiveErrorBorderColor", new ColorUIResource(0x743A3A));
    defaults.put("Component.inactiveFocusErrorColor", new ColorUIResource(0x743A3A));
    defaults.put("Focus.activeWarningBorderColor", new ColorUIResource(0xFFB62C));
    defaults.put("Component.focusWarningColor", new ColorUIResource(0xFFB62C));
    defaults.put("Focus.inactiveWarningBorderColor", new ColorUIResource(0x7F6C00));
    defaults.put("Component.inactiveFocusWarningColor", new ColorUIResource(0x7F6C00));

    defaults.put("TabbedPane.tabAreaInsets", JBUI.insets(0));
    defaults.put("TabbedPane.selectedLabelShift", 0);
    defaults.put("TabbedPane.labelShift", 0);
    defaults.put("TabbedPane.tabsOverlapBorder", true);
    defaults.put("TabbedPane.tabHeight", 32);
    defaults.put("TabbedPane.tabSelectionHeight", 2);
    defaults.put("TabbedPane.tabFillStyle", "underline");
    defaults.put("TabbedPane.fontSizeOffset", 0);
  }

  /**
   * Replace buttons
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceButtons(final UIDefaults defaults) {
    defaults.put("ButtonUI", MTButtonUI.class.getName());
    defaults.put(MTButtonUI.class.getName(), MTButtonUI.class);

    defaults.put("Button.border", new MTButtonPainter());

    defaults.put("OptionButtonUI", MTOptionButtonUI.class.getName());
    defaults.put(MTOptionButtonUI.class.getName(), MTOptionButtonUI.class);

    defaults.put("OnOffButtonUI", MTOnOffButtonUI.class.getName());
    defaults.put(MTOnOffButtonUI.class.getName(), MTOnOffButtonUI.class);
  }

  /**
   * Replace text fields
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceTextFields(final UIDefaults defaults) {
    defaults.put("TextFieldUI", MTTextFieldUI.class.getName());
    defaults.put(MTTextFieldUI.class.getName(), MTTextFieldUI.class);

    defaults.put("PasswordFieldUI", MTPasswordFieldUI.class.getName());
    defaults.put(MTPasswordFieldUI.class.getName(), MTPasswordFieldUI.class);

    defaults.put("TextField.border", new MTTextBorder());
    defaults.put("PasswordField.border", new MTTextBorder());
  }

  /**
   * Replace dropdowns
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceDropdowns(final UIDefaults defaults) {
    defaults.put("ComboBoxUI", MTComboBoxUI.class.getName());
    defaults.put(MTComboBoxUI.class.getName(), MTComboBoxUI.class);
  }

  /**
   * Replace progress bars
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceProgressBar(final UIDefaults defaults) {
    defaults.put("ProgressBarUI", MTProgressBarUI.class.getName());
    defaults.put(MTProgressBarUI.class.getName(), MTProgressBarUI.class);

    defaults.put("ProgressBar.border", new MTProgressBarBorder());
  }

  /**
   * Replace trees with custom trees with arrow styles, padding, etc
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceTree(final UIDefaults defaults) {
    defaults.put("TreeUI", MTTreeUI.class.getName());
    defaults.put(MTTreeUI.class.getName(), MTTreeUI.class);
  }

  /**
   * Install the selected item indicator in trees
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceSelectedIndicator(@NonNls final UIDefaults defaults) {
    final MTSelectedTreePainter painter = new MTSelectedTreePainter();
    defaults.put("List.sourceListSelectionBackgroundPainter", painter);
    defaults.put("List.sourceListFocusedSelectionBackgroundPainter", painter);
  }

  /**
   * Replace Table headers with padded headers
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceTableHeaders(@NonNls final UIDefaults defaults) {
    defaults.put("TableHeaderUI", MTTableHeaderUI.class.getName());
    defaults.put(MTTableHeaderUI.class.getName(), MTTableHeaderUI.class);

    defaults.put("TableHeader.border", new MTTableHeaderBorder());
    defaults.put("Table.focusSelectedCellHighlightBorder", new MTTableSelectedCellHighlightBorder());
  }

  /**
   * Replace tables with padded tables
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceTables(@NonNls final UIDefaults defaults) {
    defaults.put("TableHeader.cellBorder", new MTTableHeaderBorder());
    defaults.put("Table.cellNoFocusBorder", new MTTableCellNoFocusBorder());
    defaults.put("Table.focusCellHighlightBorder", new MTTableSelectedCellHighlightBorder());
  }

  /**
   * Replace the status bar with padded status bar
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceStatusBar(@NonNls final UIDefaults defaults) {
    defaults.put("IdeStatusBarUI", MTStatusBarUI.class.getName());
    defaults.put(MTStatusBarUI.class.getName(), MTStatusBarUI.class);
    defaults.put("IdeStatusBar.border", new MTStatusBarBorder());

    defaults.put("SeparatorUI", MTSeparatorUI.class.getName());
    defaults.put(MTSeparatorUI.class.getName(), MTSeparatorUI.class);
  }

  /**
   * Replace the spinners
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceSpinners(final UIDefaults defaults) {
    defaults.put("SpinnerUI", MTSpinnerUI.class.getName());
    defaults.put(MTSpinnerUI.class.getName(), MTSpinnerUI.class);

    defaults.put("Spinner.border", new MTSpinnerBorder());
  }

  /**
   * Replace the checkboxes.
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceCheckboxes(final UIDefaults defaults) {
    defaults.put("CheckBoxUI", MTCheckBoxUI.class.getName());
    defaults.put(MTCheckBoxUI.class.getName(), MTCheckBoxUI.class);

    defaults.put("CheckBoxMenuItemUI", MTCheckBoxMenuItemUI.class.getName());
    defaults.put(MTCheckBoxMenuItemUI.class.getName(), MTCheckBoxMenuItemUI.class);

    defaults.put("CheckBox.border", new MTCheckBoxBorder());
  }

  /**
   * Replace the radio buttons
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceRadioButtons(final UIDefaults defaults) {
    defaults.put("RadioButtonUI", MTRadioButtonUI.class.getName());
    defaults.put(MTRadioButtonUI.class.getName(), MTRadioButtonUI.class);

    defaults.put("RadioButtonMenuItemUI", MTRadioButtonMenuItemUI.class.getName());
    defaults.put(MTRadioButtonMenuItemUI.class.getName(), MTRadioButtonMenuItemUI.class);
  }

  /**
   * Replace the sliders
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceSliders(final UIDefaults defaults) {
    defaults.put("SliderUI", MTSliderUI.class.getName());
    defaults.put(MTSliderUI.class.getName(), MTSliderUI.class);
  }

  /**
   * Replace the root pane to enable the themed title bar
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceRootPane(final UIDefaults defaults) {
    defaults.put("RootPaneUI", MTRootPaneUI.class.getName());
    defaults.put(MTRootPaneUI.class.getName(), MTRootPaneUI.class);
  }

  /**
   * Replace the text areas
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceTextAreas(final UIDefaults defaults) {
    defaults.put("TextAreaUI", MTTextAreaUI.class.getName());
    defaults.put(MTTextAreaUI.class.getName(), MTTextAreaUI.class);
  }

  /**
   * Replace Tabbed Panes with Custom tabbed panes
   *
   * @param defaults of type UIDefaults
   */
  private static void replaceTabbedPanes(final UIDefaults defaults) {
    defaults.put("TabbedPane.tabInsets", JBUI.insets(5, 10, 5, 10));
    defaults.put("TabbedPane.selectedTabPadInsets", JBUI.insets(0));
    defaults.put("TabbedPane.contentBorderInsets", JBUI.insets(3, 1, 1, 1));

    defaults.put("TabbedPaneUI", MTTabbedPaneUI.class.getName());
    defaults.put(MTTabbedPaneUI.class.getName(), MTTabbedPaneUI.class);

    //    defaults.put("LabelUI", MTLabelUI.class.getName());
    //    defaults.put(MTLabelUI.class.getName(), MTLabelUI.class);
  }

  /**
   * Replace the menus with padded menus
   *
   * @param defaults defaults to fill
   */
  private static void replaceMenus(@NonNls final UIDefaults defaults) {
    defaults.put("PopupMenuUI", MTPopupMenuUI.class.getName());
    defaults.put(MTPopupMenuUI.class.getName(), MTPopupMenuUI.class);

    defaults.put("PopupMenu.border", new MTPopupMenuBorder());
    //    defaults.put("PopupMenuSeparatorUI", MTMenuSeparatorUI.class.getName());
    //    defaults.put(MTMenuSeparatorUI.class.getName(), MTMenuSeparatorUI.class);

    //    defaults.put("MenuItemUI", MTPopupMenuItemUI.class.getName());
    //    defaults.put(MTPopupMenuItemUI.class.getName(), MTPopupMenuItemUI.class);
    defaults.put("MenuItem.border", new MTMenuItemBorder());
    defaults.put("Menu.border", new MTMenuItemBorder());
  }

  /**
   * Replace icons
   *
   * @param defaults defaults to fill
   */
  @SuppressWarnings("FeatureEnvy")
  private static void replaceIcons(@NonNls final UIDefaults defaults) {
    final Icon expandIcon = MTConfig.getInstance().getArrowsStyle().getExpandIcon();
    final Icon collapseIcon = MTConfig.getInstance().getArrowsStyle().getCollapseIcon();

    defaults.put("Tree.collapsedIcon", expandIcon);
    defaults.put("Tree.expandedIcon", collapseIcon);
    defaults.put("Menu.arrowIcon", expandIcon);
    defaults.put("RadioButtonMenuItem.arrowIcon", expandIcon);
    defaults.put("CheckBoxMenuItem.arrowIcon", expandIcon);

    defaults.put("FileView.fileIcon", AllIcons.FileTypes.Unknown);
    defaults.put("Table.ascendingSortIcon", AllIcons.General.ArrowUp);
    defaults.put("Table.descendingSortIcon", AllIcons.General.ArrowDown);

    defaults.put("TextField.darcula.searchWithHistory.icon", IconLoader.getIcon("/icons/darcula/searchWithHistory.png"));
    defaults.put("TextField.darcula.search.icon", IconLoader.getIcon("/icons/darcula/search.png"));
    defaults.put("TextField.darcula.clear.icon", IconLoader.getIcon("/icons/darcula/clear.png"));
  }

  /**
   * Add registry modifications
   */
  private static void modifyRegistry() {
    Registry.get("ide.balloon.shadow.size").setValue(0);
  }

  /**
   * Method getPrefix returns the prefix of the theme in properties
   *
   * @return the prefix (type String) of the theme in properties
   */
  final String getPrefix() {
    return Objects.requireNonNull(theme).getId();
  }

  /**
   * Install defaults - background, foreground, selection background and foreground, inactive background
   *
   * @param defaults of type UIDefaults the defaults to fill
   */
  @SuppressWarnings( {"MagicCharacter",
      "DuplicateStringLiteralInspection",
      "FeatureEnvy"})
  static void loadDefaults(final UIDefaults defaults) {
    @NonNls final Map<String, Object> globalProps = new HashMap<>(100);
    final MTThemeable selectedTheme = MTConfig.getInstance().getSelectedTheme().getTheme();

    final Color backgroundColorString = selectedTheme.getBackgroundColor();
    final ColorUIResource backgroundColor = new ColorUIResource(backgroundColorString);
    globalProps.put("background", backgroundColor);
    globalProps.put("textBackground", backgroundColor);
    globalProps.put("inactiveBackground", backgroundColor);

    final Color foregroundColorString = selectedTheme.getForegroundColor();
    final ColorUIResource foregroundColor = new ColorUIResource(foregroundColorString);
    globalProps.put("foreground", foregroundColor);
    globalProps.put("textForeground", foregroundColor);
    globalProps.put("inactiveForeground", foregroundColor);
    globalProps.put("selectionForegroundInactive", foregroundColor);
    globalProps.put("selectionInactiveForeground", foregroundColor);

    final Color selectionBackgroundColorString = selectedTheme.getSelectionBackgroundColor();
    final Color selectionBgColor = new ColorUIResource(selectionBackgroundColorString);
    globalProps.put("selectionBackgroundInactive", selectionBgColor);
    globalProps.put("selectionInactiveBackground", selectionBgColor);

    final Color selectionForegroundColorString = selectedTheme.getSelectionForegroundColor();
    final Color selectionFgColor = new ColorUIResource(selectionForegroundColorString);
    globalProps.put("selectionForeground", selectionFgColor);

    for (final Object key : defaults.keySet()) {
      if (key instanceof String && ((String) key).contains(".")) {
        final String s = (String) key;
        final String property = s.substring(s.lastIndexOf('.') + 1);
        if (globalProps.containsKey(property)) {
          defaults.put(key, globalProps.get(property));
        }
      }
    }
  }
}
