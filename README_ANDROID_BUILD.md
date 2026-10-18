# MyTube - Android Jetpack Compose Video App

## How to Build the APK:

### Option A: In Android Studio
1. Click **"Download Android Studio Project (.ZIP)"** from the MyTube toolbar in this app.
2. Unzip the downloaded archive.
3. Open **Android Studio** (Hedgehog, Iguana, Koala, or Ladybug).
4. Select **File > Open** and choose the unzipped folder.
5. Wait for Gradle Sync to complete.
6. Click **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
7. Your APK will be generated at:
   `app/build/outputs/apk/debug/app-debug.apk`

### Option B: Command Line (Gradle)
Run in the project root:
```bash
./gradlew assembleDebug
```

To install directly to a connected Android phone:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```
