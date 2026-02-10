# Facy

**Facy** is an Android application that captures images using the device's camera and performs face detection using Firebase ML Kit. The project demonstrates safe handling of user media and secure integration with cloud-based ML services.

## Features

- **Capture Images**: Take pictures using the device's camera with proper permission handling.
- **Face Detection**: Detect faces in captured images using Firebase ML Kit.
- **Display Results**: Show face detection results in a user-friendly interface.
- **Secure Data Handling**: Ensures images are processed safely and sensitive data is not stored insecurely.

## Requirements

- Android Studio
- Android device or emulator with a camera
- Firebase account for Firebase ML Kit

## Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/Facy.git

	2.	Open the project in Android Studio and sync Gradle.
	3.	Configure Firebase ML Kit with your Firebase project.
	4.	Run the app on an emulator or physical device.

Security Considerations
-	Handles camera and storage permissions securely.
-	Avoids storing images or sensitive data in unencrypted storage.
-	Uses Firebase securely with proper API key management.
