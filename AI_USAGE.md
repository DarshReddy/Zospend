## AI Usage Summary

I used Gemini (Android Studio agent) and ChatGPT-5 Thinking as pair-programmers to scaffold the
module, generate MVVM/Compose boilerplate, and iterate quickly on UI/UX. They helped diagnose build
issues (KSP, missing test deps), improved performance via targeted recomposition fixes (`remember`,
`derivedStateOf`, `@Stable`), and added accessibility + haptics with minimal patches. I leaned on
the agents for small, verifiable diffs (one feature per prompt), then immediately compiled and fed
back errors for precise fix-ups. They also drafted README/usage docs and produced a concise
compliance checklist against the brief.

---

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
