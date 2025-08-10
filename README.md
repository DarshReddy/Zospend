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

*(Add images here)*

| Expense List | Expense Entry | Report         |
|--------------|---------------|----------------|
| `[List.png]` | `[Entry.png]` | `[Report.png]` |

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
    components/     # Reusable UI bits (chips, headers, empty state, etc.)
    navigation/     # NavGraph
    theme/          # Material theme
  domain/
    model/          # Expense, Category
    repo/           # ExpenseRepository, implementations
  data/
    room/           # Entities, DAO, Database (if split)
  util/             # Formatting, CSV builder, share helpers
  tests/            # Unit + UI tests
```

## Navigation

- **Start**: **List (Today)**
- **FAB**: Add → **Entry** (full-screen form)
- **Top action**: **Report**
- **Back**: Entry → List; Report → List

*(Bottom navigation is intentionally avoided; Entry is a transient task, not a top-level
destination.)*

## How to Run

1. Open in **Android Studio (latest stable)**.
2. Use **AGP 8+**, **Kotlin 2.x**, **JDK 17** recommended.
3. Sync Gradle, then Run on device/emulator (**minSdk 30**, **compileSdk 36**).

> If you must stay on Java 11, ensure Gradle toolchain and `jvmTarget` are aligned across
> Kotlin/Java.

## Decisions & Trade-offs

- **Room vs In-Memory**: Chose **Room** to satisfy persistence and mirror real usage, while keeping
  an in-memory path easy to swap behind the repository interface.
- **Date UX**: A **calendar** picker allows for quick navigation to any date, providing more
  flexibility than simple next/previous day buttons.
- **Charts**: Kept to **Canvas** for zero deps and full control.
- **Money type**: **Paise (Long)** to avoid floating point rounding issues.

## How I Used AI

AI (Gemini agent in Android Studio + ChatGPT) was used as a coding partner for scaffolding
MVVM/Compose files, refining validation/UX, and generating small, compile-ready patches. It helped
diagnose build issues (KSP, missing test deps), tuned recomposition with `remember`/
`derivedStateOf`, and added accessibility & haptics. AI also drafted the README and produced a brief
requirement compliance checklist and test scaffolds.
