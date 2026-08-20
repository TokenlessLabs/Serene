# Serene

Serene is a native Android mental-wellness and productivity app built for students. It brings planning, focus, reflection, mood check-ins, and calming audio into one approachable mobile experience.

> Serene supports wellbeing; it is not a medical or diagnostic tool.

## What it does

- Email/password and Google authentication with optional remembered sessions
- Personal goals with pending, completed, and overdue views
- Configurable Pomodoro-style work and break sessions
- Private journaling with favourites and an optional PIN lock
- Daily mood check-ins and an activity dashboard
- Customisable avatars and user profiles
- Built-in looping focus and relaxation audio
- Habit streaks designed to encourage consistent use
- Administrative user and content management
- Onboarding, settings, password reset, and terms screens

## Technology

- Java 11 and Android XML Views
- AndroidX activities, fragments, ViewBinding, Navigation, RecyclerView, and ViewPager2
- Material Components and ConstraintLayout
- Firebase Authentication and Firebase Realtime Database
- Google Sign-In
- Gradle Kotlin DSL

The app currently uses package name `com.example.serene`, has a minimum SDK of 24, and targets SDK 36.

## Architecture at a glance

Serene follows the existing activity-and-fragment structure of the Android Views ecosystem. `MainActivity` launches onboarding or authentication, while `HomeActivity` hosts the main navigation and feature fragments. Firebase data is stored beneath the authenticated user's UID so goals, journals, moods, settings, and avatar data remain user-specific. Shared managers coordinate music, avatars, streaks, and user data.

## Getting started

### Prerequisites

- Android Studio with a JDK 17 or newer (Android Studio's bundled JDK works)
- Android SDK 36
- A Firebase project with Authentication and Realtime Database enabled
- A configured Android emulator or physical device running Android 7.0 or newer

### Firebase configuration

1. Register an Android app with the package name `com.example.serene` in Firebase.
2. Download that app's `google-services.json`.
3. Place it at `app/google-services.json`. This repository tracks the Firebase Android configuration so a clone can build against the intended project; do not place service-account keys or other private credentials in the app.
4. Enable Email/Password and Google providers in Firebase Authentication.
5. Add the required SHA fingerprint for Google Sign-In in the Firebase console.
6. Create a Realtime Database and apply rules that restrict each user's private data to their authenticated UID. Review admin access separately before any public release.

### Build and run

Open the project in Android Studio, allow Gradle sync to finish, select a device, and run the `app` configuration.

From a terminal on Windows:

```powershell
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
.\gradlew.bat assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.


## Contributions

The split below identifies the two collaborators and is based on the repository's non-merge commit history. Several areas were subsequently polished or fixed by both contributors, so this describes primary implementation ownership rather than exclusive ownership.

### Meerab (`fastcel`)

- Created the project foundation, authentication UI, signup flow, Firebase setup, Google Sign-In, password reset, and remembered-login support
- Built the splash screen and onboarding flow
- Implemented avatar selection and its initial Firebase persistence
- Built the journal creation, listing, detail, and favourites functionality
- Implemented the goals data model and all/pending/completed/overdue goal flows
- Built the focus/Pomodoro feature and the main mood/dashboard experience
- Implemented core admin user-management functionality
- Built the in-app music system and integrated its playback controls
- Performed broad integration and bug-fixing across the application

### Shehryar (`shehryarhassam789`, formerly `Sherrymadlad`)

- Built the drawer and main feature navigation structure and early fragment layout
- Expanded and polished the journal screens and navigation
- Built settings and the optional journal-lock experience
- Polished goals, focus, dashboard, onboarding, avatar, and administrative interfaces
- Added terms and conditions screens and related signup integration
- Improved remembered-session, admin routing, and admin CRUD behaviour
- Implemented streak tracking and its home-screen experience
- Performed cross-feature UI and integration bug fixes

## Current scope and roadmap

Serene is currently a wellness/productivity Android application; it does not yet include an AI model or make mental-health predictions. Responsible future AI work could include opt-in, privacy-preserving reflection prompts or on-device text categorisation, with clear user control and no diagnostic claims.

Before production deployment, the project should receive a dedicated security and release-readiness pass covering Firebase rules, admin authorization, release signing, privacy disclosures, backup policy, accessibility, and end-to-end tests on multiple Android versions.

## Testing

Run the local checks with:

```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug
```

The repository currently contains the Android Studio template unit and instrumentation tests. Important manual scenarios include authentication failures, account switching, activity recreation during a focus session, journal-lock loading and failure states, Firebase offline behaviour, music lifecycle behaviour, and goal date boundaries.

## Privacy

Journals and moods are sensitive personal data. Use Firebase Security Rules that enforce per-user access, avoid logging their content, and use fictional data in demonstrations. The four-digit journal PIN is an in-app privacy control and should not be presented as encryption.

## License

No open-source license has been added. Unless the authors add one, the source remains under the authors' default copyright.
