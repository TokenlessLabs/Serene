# Serene — Project Context & Coding Instructions

## Project Overview

**Serene** is a native Android mental wellness and productivity application designed primarily for students.

The goal of Serene is to provide students with a single, calming application that helps them manage their mental wellbeing, productivity, focus, and everyday routines.

Rather than focusing on only one aspect of wellness, Serene combines several tools that students may use throughout their day, including productivity tools, self-reflection features, mood tracking, focus tools, and relaxation features.

The application should feel simple, supportive, calm, and easy to use.

---

## Core Features

Serene contains or is intended to contain the following major features.

### Goals / To-Do List

Users can create and manage goals/tasks.

The goals system includes concepts such as:

* all goals
* pending goals
* completed goals
* overdue goals
* goal status management
* progress/productivity tracking

The goals feature acts as Serene's built-in to-do list and productivity manager.

### Focus / Pomodoro

Serene includes a focus section intended to help students concentrate on study or work sessions.

This includes Pomodoro-style functionality where users can work for a defined focus period and take breaks between sessions.

Changes to timers must carefully consider Android lifecycle behavior. Do not introduce timer behavior that can unexpectedly reset, duplicate, or continue incorrectly when fragments or activities are recreated.

### Journaling

Users can write and manage personal journal entries.

The existing project contains functionality for:

* creating journal entries
* listing journal entries
* viewing journal details
* favourite journal entries
* journal locking/privacy

Journal entries should be treated as sensitive user content.

Do not weaken existing privacy or locking mechanisms when modifying journal functionality.

### Mood Tracking

Serene includes mood-tracking functionality intended to allow students to record how they are feeling and observe their emotional wellbeing over time.

Mood information should be treated as private user data.

Any future analytics, visualisations, recommendations, or AI functionality involving mood data should preserve user privacy and should not make medical diagnoses.

### Music

Serene contains an in-app music system intended to provide relaxing or focus-oriented audio without requiring users to leave the application.

Existing music functionality should be reused rather than creating a separate playback system unless explicitly approved.

Changes to music functionality should consider playback state and Android lifecycle behavior.

### Streaks and Engagement

Serene contains streak-related functionality intended to encourage consistent healthy/productive habits.

Streaks should motivate users without introducing unnecessarily stressful or punitive behaviour.

### Accounts and User Data

Serene supports user accounts and authentication.

The project currently uses Firebase services for authentication/data storage and contains Google Sign-In integration.

User-specific information must remain associated with the correct authenticated user.

### Additional Features

The project also contains functionality related to:

* onboarding
* user profiles
* avatars
* settings
* admin functionality
* terms/information screens

Before adding a new implementation, inspect the existing project to determine whether related functionality already exists.

---

# Current Technical Context

Serene is a **native Android application**.

The existing project primarily uses:

* Java
* Android XML layouts/resources
* Android Activities
* Android Fragments
* AndroidX
* ViewBinding
* ConstraintLayout
* Android Navigation components
* RecyclerView/adapters where appropriate
* Firebase Authentication
* Firebase Realtime Database
* Google Sign-In
* Gradle Kotlin DSL (`build.gradle.kts`)

The application package is currently:

`com.example.serene`

The project currently targets modern Android SDK versions while maintaining compatibility with its configured minimum SDK.

---

# Existing Project Structure

The main application code is located under:

`app/src/main/java/com/example/serene/`

Existing classes include functionality such as:

* `MainActivity`
* `HomeActivity`
* `HomeFragment`
* `Login`
* `Signup`
* `GoalsFragment`
* `AllGoalsFragment`
* `PendingGoalsFragment`
* `CompletedGoalsFragment`
* `OverdueGoalsFragment`
* `Goal`
* goal adapters
* `FocusFragment`
* `JournalFragment`
* `JournalListFragment`
* `JournalDetailFragment`
* `JournalFavouritesFragment`
* `JournalLockFragment`
* `AddJournalFragment`
* journal adapters/models
* `MusicFragment`
* `MusicManager`
* `StreakManager`
* `UserManager`
* avatar-related classes
* onboarding classes
* settings
* admin functionality

Android resources and XML layouts are located under:

`app/src/main/res/`

Before implementing anything, inspect the actual current repository because this document provides context and rules, not a complete description of every class.

---

# CRITICAL: Approval Required for Major Changes

**Do not make major changes to this project without explicit user approval.**

This is one of the most important rules for working on Serene.

A request to fix or add something does NOT automatically authorize broad refactoring.

## Changes Requiring Approval

Stop and ask for approval before:

* changing the overall architecture
* performing large-scale refactoring
* migrating Java code to Kotlin
* migrating XML UI to Jetpack Compose
* replacing Firebase
* changing the database structure/schema significantly
* changing authentication architecture
* replacing Google Sign-In/authentication mechanisms
* changing large portions of navigation
* deleting major existing functionality
* replacing existing managers or feature implementations
* moving or renaming many files/classes
* adding a major framework
* introducing a new architectural pattern across the application
* significantly changing how user data is stored
* changing security/privacy mechanisms
* rewriting an entire feature when a smaller fix is possible
* changing Gradle/build configuration in a substantial way
* making broad UI redesigns beyond the requested screen/feature
* making changes that affect several unrelated features

When a major change appears beneficial:

1. Do NOT immediately implement it.
2. Explain the problem with the current approach.
3. Explain the proposed change.
4. Explain which files/features would be affected.
5. Explain the advantages and potential risks.
6. Wait for explicit approval before proceeding.

Small, localized changes required to complete an explicitly requested task do not require separate approval.

When uncertain whether something qualifies as a major change, **ask first**.

---

# Preserve Existing Architecture

Do not unnecessarily redesign Serene's architecture.

This is an existing project, not a greenfield application.

Prefer:

**understand → reuse → modify → extend**

instead of:

**replace → rewrite → restructure**

If functionality already exists, extend or repair it whenever reasonable.

Do not create duplicate:

* managers
* adapters
* models
* authentication systems
* Firebase access layers
* navigation flows
* utility classes
* music systems
* goal systems
* journal systems

without first checking the existing implementation.

---

# Before Making Changes

For any non-trivial request, first inspect the relevant code.

Determine:

1. Which classes implement the feature?
2. Which XML layouts/resources are involved?
3. How does navigation reach the feature?
4. Is Firebase involved?
5. Is authentication state involved?
6. Are there existing models, adapters, or managers that should be reused?
7. Could the proposed change affect another feature?
8. What is the smallest safe implementation?

Do not assume architecture from class names alone.

Read the relevant implementation.

---

# Scope Control

Make the **smallest reasonable change** that correctly solves the user's request.

If asked to fix one feature, do not opportunistically refactor unrelated features.

For example, if fixing a button in the journal screen:

* fix the relevant journal code/layout
* make directly necessary supporting changes
* do not redesign the journal architecture
* do not migrate unrelated fragments
* do not reorganize the entire project

Avoid "cleanup" that substantially increases the diff unless specifically requested.

---

# UI Development Rules

Serene currently uses Android XML layouts and Views.

When modifying existing screens:

* follow the existing XML/View-based approach
* use ViewBinding where the project already uses it
* reuse existing resources
* reuse existing styles/components where practical
* maintain visual consistency
* avoid unnecessary hard-coded dimensions/colors when resources already exist
* preserve accessibility
* ensure important text remains readable
* preserve responsive behaviour across common Android screen sizes

Do **not** introduce Jetpack Compose simply because it is newer.

Migrating Serene to Compose is a major architectural/UI decision and requires explicit approval.

The desired experience should remain:

* calming
* uncluttered
* student-friendly
* approachable
* easy to navigate
* visually consistent

---

# Firebase and Data Rules

The project currently uses Firebase Authentication and Firebase Realtime Database.

Before modifying Firebase functionality:

1. Inspect how data is currently structured.
2. Determine how the authenticated user's UID is used.
3. Preserve compatibility with existing stored data where possible.
4. Avoid unnecessary database migrations.
5. Never hard-code credentials or secrets.

Do not alter the database structure significantly without approval.

Never intentionally expose:

* passwords
* authentication tokens
* API keys
* private user data
* journal content
* mood data
* other sensitive information

in logs or source code.

Do not weaken Firebase security assumptions to make a feature easier to implement.

---

# Mental Wellness Context

Serene is a **wellness application**, not a medical diagnostic application.

Features involving mood, journaling, emotional wellbeing, or future AI functionality should not:

* diagnose mental health conditions
* present uncertain conclusions as medical facts
* claim to replace professional mental healthcare
* make alarming conclusions from limited user data

The tone of user-facing wellness features should remain supportive and non-judgmental.

Treat mood and journal information as particularly sensitive data.

---

# Code Style

Follow the style already used in the surrounding project.

In general:

* prefer readable code over clever code
* keep methods focused
* use descriptive variable/method names
* avoid unnecessary abstractions
* avoid duplicated logic when an existing reusable implementation exists
* handle null/error cases appropriately
* remove unused imports introduced by changes
* do not leave debugging output in production code
* comment only where the reasoning is not obvious

Do not rewrite existing working code solely to match personal stylistic preferences.

---

# Dependencies

Avoid adding dependencies unless they provide a clear benefit.

Before adding a library:

1. Check whether Android/AndroidX or an existing dependency already provides the functionality.
2. Consider whether the feature can reasonably be implemented without another dependency.
3. Consider APK size, maintenance, security, and compatibility.

Adding a small necessary dependency is acceptable when directly required by an approved feature.

Adding or replacing major frameworks requires approval.

---

# Debugging

When asked to fix a bug:

1. Inspect the relevant implementation.
2. Identify the likely root cause.
3. Prefer fixing the root cause instead of masking the symptom.
4. Keep the fix scoped.
5. Consider lifecycle and navigation effects.
6. Consider Firebase asynchronous behaviour where applicable.
7. Check whether the fix affects related screens.
8. Explain what caused the issue and what was changed.

Do not silently rewrite an entire feature to fix a localized bug.

---

# Build and Verification

After making code changes, verify as much as the available environment allows.

At minimum:

* ensure changed Java code is syntactically valid
* ensure referenced resource IDs exist
* ensure XML remains valid
* check imports
* check navigation references when modified
* check Firebase calls when modified
* avoid obvious null/lifecycle issues
* run relevant tests when available
* run an appropriate Gradle build/check when the environment permits it

Do not claim that the application builds or that a feature works unless it was actually verified.

If verification cannot be performed, clearly state that.

---

# Communicating Changes

After implementing a non-trivial change, provide a concise summary containing:

* what was changed
* which important files were modified
* why the change was necessary
* whether anything remains to be tested manually

If a requested feature requires a significant architectural decision, present the proposed approach **before modifying the project**.

---

# Git and Repository Safety

Do not perform destructive Git operations unless explicitly requested.

Do not:

* force push
* reset branches destructively
* delete branches
* rewrite Git history
* discard unrelated local changes
* commit secrets

Do not automatically commit or push changes unless explicitly requested.

Preserve existing user changes.

---

# Decision Priority

When deciding how to modify Serene, use this priority order:

1. **User's explicit request**
2. **Safety and privacy**
3. **Preserving existing functionality**
4. **Existing Serene architecture and conventions**
5. **Smallest reasonable change**
6. **Maintainability**
7. **Newer technologies or architectural preferences**

Newer does not automatically mean better for this project.

---

# Final Instruction

Treat Serene as an existing application that should be **carefully evolved, not casually rewritten**.

Understand the existing implementation before changing it.

Reuse existing functionality whenever practical.

Keep requested changes focused.

Most importantly:

> **Never make a major architectural, structural, database, authentication, or large-scale UI change without explaining it and receiving explicit user approval first.**
