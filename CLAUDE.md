# CrossLine - Project Rules & Specifications

## Project Overview
- **App Name**: CrossLine (크로스라인)
- **Concept**: AI-driven self-management & Secure offline field reporting
- **Platform**: Android (Jetpack Compose, Kotlin)
- **Distribution**: Personal use only (not for Play Store)

## Architecture
- **UI**: Jetpack Compose (Material 3)
- **Database**: Room (main DB for Diet/MoneyTrack) + Encrypted Room (Pickup only)
- **DI**: Hilt
- **Navigation**: Compose Navigation
- **AI Integration**: ChatGPT API (OpenAI)
- **Encryption**: AES-256 for Pickup data

## Session Structure

### 1. Diet Session
- Good/Bad meal logging with photo capture
- GPT API meal photo analysis (calorie/nutrition estimation)
- **Pivot Camera**: Body-check camera with user-defined reference lines (horizontal/vertical grid)
- **Ghost Overlay**: Previous photo overlay for alignment comparison
- InBody data recording
- Condition score (1-5 scale)

### 2. MoneyTrack Session
Three sub-sections:

#### 2a. Study (학습)
- Target: Industrial Safety Engineer certification (산업안전기사)
- Written/Practical exam previous-question rotation checklist
- Study archive: Manual copy-paste storage of external AI study logs

#### 2b. AI Business (AI 창업)
- Per-project management (e.g., MeongnyangSignal)
- 7 management tabs per project:
  1. MVP Development
  2. Office Space
  3. Government Grants
  4. Marketing/Channels
  5. Business Model
  6. Network/Mentors
  7. Legal/Admin

#### 2c. Investment (투자)
- Trading automation program development log
- Update history management

### 3. Pickup Session (Run & Miss)
- Offline-only field reporting
- Classification: Run (Success) / Miss (Fail)
- **Silent Shutter Camera**: White-frame timestamp overlay with draggable position
- **SECURITY**: Data NEVER sent to any AI API. AES-256 encrypted local storage only.

## AI Assistant
- Floating widget on main dashboard
- Quick-summon button available in all sessions EXCEPT Pickup
- Long-term memory: End-of-day JSON summary saved locally, loaded next day
- Roles: Data structuring, daily briefing, strategic advice

## Security & Stealth Features
- **Dummy Screen**: App launches into a fake Memo/Notepad UI
- **Secret Gesture**: Required to access real app content
- **Emergency Switch**: Flipping phone face-down instantly switches to MoneyTrack dashboard
- **Pickup Isolation**: Pickup data is completely isolated from AI pipeline

## Development Rules
1. Pickup data must NEVER touch any network API - enforce at architecture level
2. All Pickup-related database tables use a separate encrypted Room database
3. AI assistant context must exclude any Pickup references
4. Silent camera implementation has no platform restrictions (personal use)
5. Sensor-based gestures (flip detection) use Android SensorManager
6. Keep UI minimal and functional - no unnecessary decoration
7. Korean language for user-facing strings, English for code/comments
