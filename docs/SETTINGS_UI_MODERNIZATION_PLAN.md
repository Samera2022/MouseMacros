# Settings Dialog Modernization Plan

## Current Problems

### 1. Visual Weight & Hierarchy
- Everything has the same visual weight (all bold, same-sized text)
- Too many bold section titles: Settings / Appearance / System / Macro / Quick Actions / Information & Help / Advanced
- Separator lines and card borders create visual noise
- Can''t tell what to focus on first

### 2. Space Utilization
- Window is too wide (~1000px) but content only needs ~760-820px
- Bottom three cards (Quick Actions / Information & Help / Advanced) take huge space but only contain a few buttons
- Overall layout feels empty yet crowded at the same time

### 3. Control Inconsistency
- "Switch Language" dropdown is too narrow
- "Window Resizing Mode" dropdown is too wide
- Number input fields have random widths
- Looks like "controls with default sizes stitched together"

### 4. Visual Clutter
- Too many borders: card borders, separator lines, control borders
- All controls have similar gray backgrounds and borders
- Lack of depth: everything blends together

### 5. Typography Issues
- Almost everything is bold (font-weight: 600/700)
- Section headers, labels, even some button text all bold
- Should use weight hierarchy: 600 for titles, 500 for labels, 400 for secondary text

## Five Priority Fixes

### 1. Content Width → 800px Max
**What**: Center content area with max-width ~760-820px  
**Why**: Current 1000+px width is excessive for settings  
**How**: Wrap content in centered container with max-width

**Before**:
```
┌──────────────────────────────────────────────────────────────────┐
│ Settings                                                         │
│ [...........................................................]     │
└──────────────────────────────────────────────────────────────────┘
```

**After**:
```
┌──────────────────────────────────────────────────────────────────┐
│         Settings                                                 │
│         [.................................]                      │
└──────────────────────────────────────────────────────────────────┘
```

### 2. Unified "Label Left, Control Right" Layout
**What**: All settings use same row structure  
**Why**: Current mix of inline/stacked layouts is inconsistent  
**How**: GridBagLayout with label (left, weight=1.0) and control (right, weight=0)

**Current Problems**:
```
Follow System Settings   □
       Switch Language [xxx]
Enable Dark Mode         ☑
```

**Target**:
```
Theme                                      [ System ▼ ]
Language                                   [ English ▼ ]
Long tooltips                              [ ●─── ]
```

### 3. Remove Card Borders & Excessive Separators
**What**: Delete the three big cards at bottom, reduce separator lines  
**Why**: Cards create visual clutter and waste space  
**How**: 
- Delete card borders around Quick Actions / Information / Advanced
- Use 24-32px whitespace between sections instead of lines
- Keep sections subtle or remove separators entirely

**Before**:
```
┌─────────────────────────────────────────┐
│ ⚡ Quick Actions                         │
│ [⌨️ Custom Hotkey]  [🧭 Show Wizard]    │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│ ℹ️ Information & Help                    │
│ [ℹ️ About]  [🔄 Update]  [📄 Export Log] │
└─────────────────────────────────────────┘
```

**After**:
```
Shortcuts

Keyboard shortcut                         [ Configure ]
Welcome wizard                            [ Open ]

About

Author                                              >
Update information                                  >
Export log                                          >
```

### 4. Checkbox → Toggle, Theme Merge
**What**: 
- Replace checkboxes with toggle switches
- Merge "Follow System" + "Dark Mode" into single "Theme" dropdown

**Why**: 
- Toggles are more appropriate for on/off settings
- Checkboxes feel like "form multi-select"
- Two separate theme controls is confusing

**Current**:
```
Follow System Settings   □
Enable Dark Mode         ☑
```

**Target**:
```
Theme                                      [ Dark ▼ ]
  Options: System | Light | Dark
```

Or toggle-style buttons:
```
Theme                            [ System | Light | Dark ]
                                             ^^^^^ selected
```

### 5. Font Weight & Control Sizing
**What**: Reduce font weights and normalize control sizes  
**Why**: Everything bold makes nothing stand out  
**How**:
- Page title: 24px / weight 600
- Section headers: 16-18px / weight 600
- Setting labels: 14px / weight 400-500
- Secondary text: 12px / weight 400 / gray color
- Buttons: 13-14px / weight 500

**Control Heights**:
- Dropdowns / inputs: 32px
- Buttons: 32-36px
- Toggles: 24px

## Detailed Design Spec

### Target Layout Structure

```text
┌──────────────────────────────────────────────────────────┐
│  Settings                                           ×    │  ← 24px bold
│                                                          │
│  Appearance                                              │  ← 16px bold
│                                                          │
│  Theme                                      [ Dark    ▼ ]│  ← 14px normal
│  Language                                   [ English ▼ ]│
│  Long tooltips                              [    ●─── ]  │  ← Toggle switch
│  Show additional information               ← 12px gray   │
│  Window resizing                            [ Normal  ▼ ]│
│                                                          │
│  System                                                  │
│                                                          │
│  Default macro storage                      [ ───○ ]     │  ← Toggle OFF
│                                                          │
│  Macro                                                   │
│                                                          │
│  Quick mode                                 [ ───○ ]     │
│  Fast execution without confirmation       ← 12px gray   │
│  Custom macro settings                      [ ───○ ]     │
│                                                          │
│  Shortcuts                                               │
│                                                          │
│  Keyboard shortcut                         [ Configure ] │  ← Button
│  Welcome wizard                            [ Open      ] │
│                                                          │
│  About                                                   │
│                                                          │
│  Author                                      >           │  ← Navigation row
│  Update information                          >           │
│  Export log                                  >           │
│                                                          │
│  Advanced                                                │
│  Script manager                              >           │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

### Expandable Settings

When toggles are ON, show nested controls:

```text
Custom macro settings                      [ ●─── ]

  Repeat                                     [  1   ]
  Repeat delay                               [ 0.0s ]
```

Indented 24px from left, slightly smaller/grayed labels.

### Color Scheme (Dark Mode Example)

```
Window BG:        #181A1F
Content BG:       #181A1F

Control BG:       #242830
Control Hover:    #2C313B
Border:           #363C47

Primary Text:     #F0F2F5
Secondary Text:   #9DA5B4
Disabled Text:    #626A78

Accent:           #6C8CFF (focus states, active toggles)
```

**Border Philosophy**:
- Default state: very subtle border (#363C47)
- Focus state: accent border (#6C8CFF)
- No thick borders on cards or sections

### Typography Scale

| Element | Size | Weight | Color | Notes |
|---------|------|--------|-------|-------|
| Page Title | 24px | 600 | Primary | "Settings" |
| Section Header | 16-18px | 600 | Primary | "Appearance", "System" |
| Setting Label | 14px | 400-500 | Primary | "Theme", "Language" |
| Sub-label | 12px | 400 | Secondary | Descriptions under toggles |
| Button Text | 13-14px | 500 | Primary | Action buttons |
| Dropdown Text | 13px | 400 | Primary | Inside dropdowns |

### Button Styles

**Primary Action** (Save):
- Background: Accent color
- Height: 36px
- Padding: 12px 24px

**Secondary Action** (Cancel, Configure, Open):
- Background: Control BG
- Border: subtle
- Height: 32px
- Padding: 8px 16px

**Navigation Row** (Author, Update Info, etc.):
- No button, just label + arrow
- Entire row clickable
- Hover: background becomes Control Hover
- Cursor: pointer

### Control Widths

| Control Type | Width | Notes |
|--------------|-------|-------|
| Dropdown (short) | 120-160px | Theme, Language |
| Dropdown (long) | 180-220px | Window Resizing |
| Toggle Switch | 44px | Standard toggle size |
| Number Input | 60-80px | Repeat, Delay |
| File Path | Flexible | Takes remaining space |
| Action Button | 80-120px | Configure, Open, Browse |

## Implementation Notes

### Java Swing Constraints

1. **Toggle Switches**: Swing doesn''t have native toggles. Options:
   - Use styled JCheckBox with custom icon
   - Use JButton with toggle behavior
   - Use third-party component library

2. **Rounded Borders**: `BorderFactory.createLineBorder(color, 1, true)`
   - Third param `true` enables rounded corners

3. **Layout Manager**: 
   - Main content: BoxLayout (vertical sections)
   - Each row: GridBagLayout (label left, control right)
   - Nested controls: BoxLayout with left padding

4. **Font Rendering**:
   - Use deriveFont() for size/weight changes
   - Plain = 400, Bold = 600-700
   - Some systems don''t support weight between 400-700

### Expandable Sections

```java
JPanel expandableContent = new JPanel();
expandableContent.setVisible(false);

toggle.addActionListener(e -> {
    expandableContent.setVisible(toggle.isSelected());
    container.revalidate();
    container.repaint();
});
```

### Theme Dropdown Logic

```java
JComboBox<String> themeCombo = new JComboBox<>(new String[]{"System", "Light", "Dark"});

themeCombo.addActionListener(e -> {
    String selected = (String) themeCombo.getSelectedItem();
    if ("System".equals(selected)) {
        ConfigManager.set("follow_system_settings", true);
    } else {
        ConfigManager.set("follow_system_settings", false);
        ConfigManager.set("enable_dark_mode", "Dark".equals(selected));
    }
});
```

## Before/After Comparison

### Current Issues
- ❌ Width too wide (1000+px)
- ❌ Mixed layout styles (inline, stacked, random)
- ❌ Giant card borders waste space
- ❌ All text is bold
- ❌ Control sizes inconsistent
- ❌ Too many separators and borders
- ❌ Checkboxes for on/off states
- ❌ Two separate theme controls

### After Improvements
- ✅ Centered 800px max width
- ✅ Consistent "label left, control right"
- ✅ Minimal borders, whitespace separation
- ✅ Font hierarchy (title > section > label > secondary)
- ✅ Uniform control sizes (32px height)
- ✅ Clean, minimal separators
- ✅ Toggle switches for on/off
- ✅ Single merged theme dropdown

## Phased Implementation

### Phase 1: Layout Foundation (30-45 min)
1. Add centered content wrapper with 800px max-width
2. Convert all setting rows to GridBagLayout (label left, control right)
3. Remove card borders from bottom buttons
4. Replace with flat navigation rows (label + arrow)

### Phase 2: Controls & Typography (30-45 min)
5. Merge theme controls into single dropdown
6. Implement toggle switch behavior (styled JCheckBox)
7. Normalize all control sizes (32px height)
8. Apply font weight hierarchy (remove most bold)

### Phase 3: Spacing & Polish (20-30 min)
9. Replace separator lines with whitespace (24-32px)
10. Add expandable sections for nested settings
11. Implement hover states for navigation rows
12. Fine-tune padding and spacing

### Phase 4: Colors & Accessibility (Optional)
13. Apply subtle color scheme (if dark mode active)
14. Add focus indicators (accent color borders)
15. Test keyboard navigation
16. Verify color contrast ratios

## Next Steps

1. **Review this design spec** - Confirm the direction
2. **Pick phase to start** - Implement incrementally or all at once?
3. **Test along the way** - Build + run after each major change
4. **Iterate based on visual result** - Adjust spacing/sizes as needed

The goal is to transform from "debug panel" to "polished settings page" while keeping the implementation practical for Swing''s limitations.