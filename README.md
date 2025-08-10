# Zospend — Smart Daily Expense Tracker (Android)

A full-featured expense module for small business owners to quickly **capture, review, and report**
daily spend. Built with **Jetpack Compose** and **MVVM**, with local persistence via **Room**.

## AI Usage Summary

I used Gemini (Android Studio agent) and ChatGPT-5 Thinking as pair-programmers to scaffold the
module, generate MVVM/Compose boilerplate, and iterate quickly on UI/UX. They helped diagnose build
issues (KSP, missing test deps), improved performance via targeted recomposition fixes (`remember`,
`derivedStateOf`, `@Stable`), and added accessibility + haptics with minimal patches. I leaned on
the agents for small, verifiable diffs (one feature per prompt), then immediately compiled and fed
back errors for precise fix-ups. They also drafted README/usage docs and produced a concise
compliance checklist against the brief.

## Prompt Log (Key Prompts & Outcomes)

1. **Prompt:** “Add a calendar icon to the expense list screen. On click, it should open a date
   picker that allows selection up to today.”
   **Outcome:** Implemented a `DatePickerDialog` launched from a new calendar icon, disabling future
   dates and updating the ViewModel on selection.

2. **Prompt:** “I’m getting the following error while building my project: KSP failed with exit
   code: PROCESSING_ERROR.”
   **Outcome:** Root-caused annotation processing issues and provided a checklist (versions,
   kapt/ksp setup, incremental flags), plus a minimal Gradle/config patch.

3. **Prompt:** “Review Entry/List/Report composables for recomposition issues; introduce `remember`/
   `derivedStateOf` where appropriate.”
   **Outcome:** Produced a focused diff using `remember`, `derivedStateOf`, and `@Stable` data
   holders to reduce unnecessary recompositions.

4. **Prompt:** “Refactor the chart to calculate its Y-axis scale automatically. Also, replace the
   hardcoded category colors with theme-aware ones.”
   **Outcome:** Made the line chart’s Y-axis dynamic by calculating intervals based on the data.
   Decoupled colors from the data model and used theme-aware colors in the UI.

5. **Prompt:** “Add content descriptions for actionable icons/images, error semantics for
   validation, and haptic feedback on successful add.”
   **Outcome:** Inserted accessibility modifiers, announced validation errors via semantics, and
   added light haptics on success.

6. **Prompt:** “Add category icons in enum.”
   **Outcome:** Extended `Category` enum with icon refs and updated UI (List/Report) to render icons
   next to labels.

7. **Prompt:** “Add Material icons.”
   **Outcome:** Verified BOM and `material-icons-extended` dependency; exposed icons for immediate
   use.

8. **Prompt:** “Add unit tests for InMemoryExpenseRepository and a simple Compose UI test for
   EntryScreen.”
   **Outcome:** Added repository tests (success, duplicate rejection, per-day totals) and a Compose
   test for validation/success flow; included `kotlinx-coroutines-test`.

9. **Prompt:** “`mock` is unresolved.”
   **Outcome:** Identified missing Mockito Kotlin dependency; added minimal Gradle entry and
   imports.

10. **Prompt:** “Compare current code to the Zobaze brief and output a checklist with Pass/Fail per
    requirement.”
    **Outcome:** Generated a gap report with file/line pointers to close misses (export action,
    empty states, grouping behavior).

11. **Prompt:** “Generate README.md for the module (features, architecture, how to run,
    decisions/trade-offs, ‘How I used AI’).”
    **Outcome:** Produced a concise README tailored to the current codebase.

12. **Prompt:** “Show receipt thumbnail in today’s list item; on click open dialog with full image.”
    **Outcome:** Added a thumbnail to the list item that, when clicked, opens a dialog showing the
    full-sized receipt image.

> Notes: Prompts were consolidated and outcomes were tightened for clarity.

## Features

**Core**

- **Add Expense**: Title, amount (₹), category (Staff/Travel/Food/Utility), optional notes, optional
  receipt image.
- **Daily List**: View today’s expenses by default; select a date using a **calendar**; totals +
  count; empty state.
- **Report (7 days)**: Daily totals + category-wise totals; simple **Canvas** line chart showing
  category trends.
- **Export**: Share **CSV** (daily + category totals) via Android share sheet.

**Polish**

- **Receipt previews**: Thumbnail in list; tap to view full image in a dialog.
- **Duplicate detection**: Prevents accidental double entries (same title+amount+day within 5
  minutes).
- **Accessibility**: Content descriptions, error semantics; light haptics on successful add.
- **Modern UI**: Pure Compose, Material 3, micro-animations (e.g., add confirmation chip).
- **Tests**: Unit tests (repository) + a basic Compose UI test (entry validation & success).

## Feature Checklist

- [x] **Expense Entry**: Add new expenses with title, amount, category, notes, and an optional
  receipt image.
- [x] **Daily Expense List**: View expenses for any selected day using a calendar picker.
- [x] **Daily Totals**: See the total amount and count of expenses for the selected day.
- [x] **7-Day Report**: Visualize category spending trends over the last week with a line chart.
- [x] **CSV Export**: Share daily and category totals via the Android share sheet.
- [x] **Dynamic Chart Scaling**: The report chart's Y-axis automatically adjusts to the data.
- [x] **Theme-Aware UI**: Colors adapt to the system's light or dark theme.
- [x] **Image Previews**: View receipt thumbnails in the list and tap to see the full image.
- [ ] **Search & Filter**: Search for expenses by title or filter by category.
- [ ] **Monthly Reports**: View aggregated expense data on a monthly basis.
- [ ] **Cloud Sync**: Back up and sync expenses across multiple devices.

## APK Download Link

## Screenshots

| Expense List                                                                                                                                            | Expense Entry                                                                                                                                           | Report                                                                                                                                                  |
|---------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| <img width="216" height="480" alt="Screenshot_20250810_124111" src="https://github.com/user-attachments/assets/a4625acf-44d9-4d34-84a1-a804d7226b44" /> | <img width="216" height="480" alt="Screenshot_20250810_124348" src="https://github.com/user-attachments/assets/5f0cfa30-9cdc-49fe-b6bd-ef8828c99cb8" /> | <img width="216" height="480" alt="Screenshot_20250810_124220" src="https://github.com/user-attachments/assets/dbde7880-9728-4ae6-b3f4-6fb46477d8fb" /> |

<img width="216" height="480" alt="Screenshot_20250810_124452" src="https://github.com/user-attachments/assets/b56d75ac-dc62-47c0-9f32-a6ef65f7eacf" />
<img width="216" height="480" alt="Screenshot_20250810_124428" src="https://github.com/user-attachments/assets/1288d9e1-3b75-432a-812d-dcc617c43f90" />
<img width="216" height="480" alt="Screenshot_20250810_124420" src="https://github.com/user-attachments/assets/5e3e6431-032c-476f-9ccb-689034694810" />
<img width="216" height="480" alt="Screenshot_20250810_124242" src="https://github.com/user-attachments/assets/bb844705-7c17-4f8c-8164-5c741038c58f" />


## Architecture & Tech

- **Architecture**: MVVM with `ViewModel` + `StateFlow`.
- **UI**: Jetpack Compose (Material 3), Navigation-Compose.
- **Data**: Room database as single source of truth; repository abstraction.
- **Time/Money**: `java.time` (Instant/LocalDate); amounts stored in **paise/cents (Long)** for
  accuracy.
- **Charts**: Compose `Canvas` (no external chart libs).
- **Export**: CSV string → Android share intent.

### Module Structure

```
app/
  ui/
    entry/          # EntryScreen, EntryViewModel
    list/           # ExpenseListScreen, ExpenseListViewModel
    report/         # ReportScreen, ReportViewModel
    components/     # Reusable UI bits (dropdowns, dialogs, chart etc.)
    navigation/     # NavGraph
    theme/          # Material theme
    main/           # Main screen, Wrapper screen
  domain/
    model/          # Expense, Category
    repo/           # ExpenseRepository, implementations
  data/
    local/          # Entities, DAO, Database
    mock/           # Mock data creation 
  util/             # Formatting, CSV builder, share helpers
  tests/            # Unit + UI tests
```

## Navigation

- **Bottom Nav**: **List (Today by Default)** **Report**
- **FAB**: Add → **Entry** (bottom sheet form)
- **Update Expense**: Full screen update expense form
- **Back**: Entry → List; Report → List

## How to Run

1. Open in **Android Studio (latest stable)**.
2. Use **AGP 8+**, **Kotlin 2.x**, **JDK 17** recommended.
3. Sync Gradle, then Run on device/emulator (**minSdk 30**, **compileSdk 36**).

> If you must stay on Java 11, ensure Gradle toolchain and `jvmTarget` are aligned across
> Kotlin/Java.

## Decisions & Trade-offs

- **Room vs In-Memory**: Chose **Room** to satisfy persistence and mirror real usage.
- **Date UX**: A **calendar** picker allows for quick navigation to any date, providing more
  flexibility than simple next/previous day buttons.
- **Charts**: Kept to **Canvas** for zero dependencies and full control.
- **Money type**: **Paise/Cents (Long)** to avoid floating point rounding issues.