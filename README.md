# 💬 CometChat Android App – Real-time Messaging Application

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![CometChat](https://img.shields.io/badge/CometChat-UI%20Kit%205.1.0-orange.svg)](https://www.cometchat.com)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)

A modern real-time chat application built using **CometChat UI Kit** and **Kotlin** with **MVVM architecture**, showcasing features like real-time messaging, voice/video calling, and seamless UI.

---

## 🚀 Features

### 💬 Messaging
- Real-time chat with delivery/read receipts
- One-to-one and group conversations
- Typing indicators and message status
- Multimedia sharing (images, videos, files)

### 👥 User Management
- Browse and connect with users
- User profile viewer
- Programmatic test user creation

### 📞 Audio/Video Communication
- Voice and video calls with CometChat Calls SDK
- Call logs and call duration tracking
- Call control options (mute, speaker, switch camera)

### 🎨 UI and UX
- Material Design 3
- Bottom navigation bar with 4 main tabs
- Responsive layouts for all screen sizes
- Dark/Light theme support

---

## 🧱 Architecture

This app follows **MVVM (Model-View-ViewModel)** and **Clean Architecture** principles.

```
📦 com.example.cometchatapp/
├── CometChatApplication.kt       # SDK initialization
├── data/
│   └── repository/
│       └── ChatRepository.kt     # Data layer
├── ui/
│   ├── MainActivity.kt           # Hosts bottom navigation
│   ├── MessageActivity.kt        # Chat screen
│   ├── viewmodel/
│   │   ├── MainViewModel.kt
│   │   └── MessageViewModel.kt
│   └── fragments/
│       ├── ChatsFragment.kt
│       ├── CallLogsFragment.kt
│       ├── UsersFragment.kt
│       └── GroupsFragment.kt
└── res/                          # Layouts, themes, etc.
```

---

## 🛠️ Tech Stack

- **Language:** Kotlin  
- **UI:** Android Views + Material Design 3  
- **Architecture:** MVVM + Repository Pattern  
- **Navigation:** Android Navigation Component  
- **Real-time Chat:** CometChat UI Kit  
- **Calls:** CometChat Calls SDK  
- **Asynchronous:** Kotlin Coroutines


## 🧑‍💻 Prerequisites

- Android Studio Arctic Fox or later
- JDK 8 or higher
- Android SDK 21+
- A [CometChat Account](https://app.cometchat.com/)

---

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/DevDewangan16/CometChatDemo
```

### 2. Configure CometChat

#### Create App
- Visit [CometChat Dashboard](https://app.cometchat.com/)
- Create an app and note:
  - App ID
  - Region (us, eu, etc.)
  - Auth Key

#### Update Credentials
Edit `CometChatApplication.kt`:

```kotlin
companion object {
    private const val APP_ID = "YOUR_APP_ID"
    private const val REGION = "YOUR_REGION"
    private const val AUTH_KEY = "YOUR_AUTH_KEY"
}
```

### 3. Run the App
- Open in Android Studio
- Sync Gradle
- Run on emulator or device
- App will auto-login using test users


## 📖 Usage Guide

### Launch Flow
1. App initializes CometChat and creates test users
2. User is auto-logged in
3. Bottom navigation gives access to Chats, Users, Groups, and Calls

---

## 🐞 Troubleshooting

| Issue | Solution |
|-------|----------|
| `CometChat initialization failed` | Check App ID, Region, and Auth Key |
| `User does not exist` | Ensure test user is being created before login |
| `Network request failed` | Check internet connection and `network_security_config.xml` |


## 📞 Support

### Helpful Links
- 📚 [CometChat Docs](https://www.cometchat.com/docs/)
- 📱 [Android Guide](https://developer.android.com/guide)
- 🏗️ [Architecture Guide](https://developer.android.com/topic/libraries/architecture)

Built with ❤️ using <a href="https://www.cometchat.com/">CometChat</a> & <a href="https://android.com">Android</a>
</div>
