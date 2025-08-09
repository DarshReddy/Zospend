# AI_USAGE.md

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

1. **Prompt:** “Add next and previous buttons in expense list screen to change days and see expenses
   list.”  
   **Outcome:** Implemented date navigation across ViewModel/UI; wired repository query for selected
   day; added simple UX affordances.

2. **Prompt:** “I’m getting the following error while building my project: KSP failed with exit
   code: PROCESSING_ERROR.”  
   **Outcome:** Root-caused annotation processing issues and provided a checklist (versions,
   kapt/ksp setup, incremental flags), plus a minimal Gradle/config patch.

3. **Prompt:** “Review Entry/List/Report composables for recomposition issues; introduce `remember`/
   `derivedStateOf` where appropriate.”  
   **Outcome:** Produced a focused diff using `remember`, `derivedStateOf`, and `@Stable` data
   holders to reduce unnecessary recompositions.

4. **Prompt:** “Make the changes.”  
   **Outcome:** Applied the proposed recomposition optimizations as a compile-ready patch.

5. **Prompt:** “Add content descriptions for actionable icons/images, error semantics for
   validation, and haptic feedback on successful add.”  
   **Outcome:** Inserted accessibility modifiers, announced validation errors via semantics, and
   added light haptics on success.

6. **Prompt:** “Do the changes, you only gave idea.”  
   **Outcome:** Delivered the concrete code changes touching only the relevant composables and
   helpers.

7. **Prompt:** “Add category icons in enum.”  
   **Outcome:** Extended `Category` enum with icon refs and updated UI (List/Report) to render icons
   next to labels.

8. **Prompt:** “Add Material icons.”  
   **Outcome:** Verified BOM and `material-icons-extended` dependency; exposed icons for immediate
   use.

9. **Prompt:** “Add unit tests for InMemoryExpenseRepository and a simple Compose UI test for
   EntryScreen.”  
   **Outcome:** Added repository tests (success, duplicate rejection, per-day totals) and a Compose
   test for validation/success flow; included `kotlinx-coroutines-test`.

10. **Prompt:** “mock is unresolved.”  
    **Outcome:** Identified missing Mockito Kotlin dependency; added minimal Gradle entry and
    imports.

11. **Prompt:** “Compare current code to the Zobaze brief and output a checklist with Pass/Fail per
    requirement.”  
    **Outcome:** Generated a gap report with file/line pointers to close misses (export action,
    empty states, grouping behavior).

12. **Prompt:** “Generate README.md for the module (features, architecture, how to run,
    decisions/trade-offs, ‘How I used AI’).”  
    **Outcome:** Produced a concise README tailored to the current codebase.

13. **Prompt:** “Create a 3–5 sentence AI usage summary and a prompt log; return as AI_USAGE.md.”  
    **Outcome:** Generated this document with an edited, reviewer-friendly log.

14. **Prompt:** “Show receipt thumbnail in today’s list item; on click open dialog with full image.
    Make bottom nav icons filled/outline based on selection.”  
    **Outcome:** Added thumbnail + dialog preview flow; if bottom navigation is enabled, toggled
    filled/outlined icons based on current destination.

> Notes: Items 4 and 6 represent “apply the patch” follow-ups to earlier design prompts. Duplicated
> prompts were consolidated; outcomes were tightened for clarity.
