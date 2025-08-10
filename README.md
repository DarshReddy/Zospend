# Zospend — Smart Daily Expense Tracker (Android)

A full-featured expense module for small business owners to quickly **capture, review, and report**
daily spend. Built with **Jetpack Compose** and **MVVM**, with local persistence via **Room**.

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

## Screenshots

| Expense List | Expense Entry | Report         |
|--------------|---------------|----------------|
|  <img width="216" height="480" alt="Screenshot_20250810_124111" src="https://github.com/user-attachments/assets/a4625acf-44d9-4d34-84a1-a804d7226b44" />| <img width="216" height="480" alt="Screenshot_20250810_124348" src="https://github.com/user-attachments/assets/5f0cfa30-9cdc-49fe-b6bd-ef8828c99cb8" /> | <img width="216" height="480" alt="Screenshot_20250810_124220" src="https://github.com/user-attachments/assets/dbde7880-9728-4ae6-b3f4-6fb46477d8fb" /> |

<img width="216" height="480" alt="Screenshot_20250810_124452" src="https://github.com/user-attachments/assets/b56d75ac-dc62-47c0-9f32-a6ef65f7eacf" />
<img width="216" height="480" alt="Screenshot_20250810_124428" src="https://github.com/user-attachments/assets/1288d9e1-3b75-432a-812d-dcc617c43f90" />
<img width="216" height="480" alt="Screenshot_20250810_124420" src="https://github.com/user-attachments/assets/5e3e6431-032c-476f-9ccb-689034694810" />
<img width="216" height="480" alt="Screenshot_20250810_124242" src="https://github.com/user-attachments/assets/bb844705-7c17-4f8c-8164-5c741038c58f" />


## Architecture & Tech

- **Architecture**: MVVM with `ViewModel` + `StateFlow`.
- **UI**: Jetpack Compose (Material 3), Navigation-Compose.
- **Data**: Room database as single source of truth; repository abstraction.
- **Time/Money**: `java.time` (Instant/LocalDate); amounts stored in **paise (Long)** for accuracy.
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
- **FAB**: Add → **Entry** (bottomsheet form)
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
- **Charts**: Kept to **Canvas** for zero deps and full control.
- **Money type**: **Paise/Cents (Long)** to avoid floating point rounding issues.

## How I Used AI

AI (Gemini Agent in Android Studio + ChatGPT) was used as a coding partner for scaffolding
MVVM/Compose files, refining validation/UX, and generating small, compile-ready patches. It helped
diagnose build issues (KSP, missing test deps), tuned recomposition, and added accessibility & haptics. AI also drafted the README and produced a brief
requirement compliance checklist and test scaffolds.
