### 1. Create Ionic App & Run Android App

```
npm install -g @ionic/cli

ionic start capacitor-widget-github-notification blank --type vue --capacitor

cd capacitor-widget-github-notification

yarn install

ionic cap add android

ionic cap open android

ionic cap sync android

ionic cap run android -l --external
```

### 2. Implementation Gihub Login

```
yarn add @capacitor-firebase/authentication firebase capacitor-secure-storage-plugin
```

[✨ Github SignIn](https://github.com/sawaca96/capacitor-widget-github-notification/commit/82e1679e76ae7a0d2ebb86219dc6d812ca72f05b)

### 3. Create Widget

| 1                                                                                                               | 2                                                                                                               | 3                                                                                                               | 4                                                                                                               | 5                                                                                                               |
| --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| ![image](https://user-images.githubusercontent.com/49309322/193579408-5d6271b7-b877-469f-8aaa-8d7ee6e96a57.png) | ![image](https://user-images.githubusercontent.com/49309322/193579707-4c3ec37b-f255-4ce3-8c61-91d0592b4b95.png) | ![image](https://user-images.githubusercontent.com/49309322/193579859-f4f63cb8-f408-4b92-bfde-d8a45c59974f.png) | ![image](https://user-images.githubusercontent.com/49309322/193581041-37db8d94-a278-4965-8147-d1a16a945626.png) | ![image](https://user-images.githubusercontent.com/49309322/193580216-03a343bd-7070-4228-a6b5-9bff0bfeda76.png) |

[🔧 Update widget config](https://github.com/sawaca96/capacitor-widget-github-notification/commit/a5956d6f3eb0bd60fd7071e804d12a1bd08b1eba)

### 4. Make Vector Asset

| 1                                                            | 2                                                                                                               | 3                                                                                                               |
| ------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| Download SVG from [Octicons](https://primer.style/octicons/) | ![image](https://user-images.githubusercontent.com/49309322/193585120-b06efe4c-5c04-4e2c-80e0-e28fd207ce06.png) | ![image](https://user-images.githubusercontent.com/49309322/193585552-af109fbd-c24a-4a42-b587-741f58e4f8f3.png) |

### 5-1. Header Design

```
LinearLayout(Root)
  ㄴ LinearLayout(Header)
    ㄴ ImageView(Github Icon)
    ㄴ LinearLayout(Title And Updated At)
      ㄴ TextView(Title)
      ㄴ TextView(Updated At)
    ㄴ ImageButton(Sync Icon)
```

<details>
<summary>
Design Spec
</summary>

**Root**

- component: LinearLayout(vertical)
- xmlns:app=xmlns:android="http://schemas.android.com/apk/res/android"

**Header**

- component: LinearLayout(horizontal)
- height: 40dp
- background: #292929
- paddingVertical: 4dp
- paddingHorizontal: 10dp
- weightSum: 10

**Github Icon**

- component: ImageView
- layout_height: match_parent
- layout_weight: 1
- padding: 4dp
- scaleType: fitCenter
- src:

**Title And Updated At**

- component: LinearLayout(vertical)
- marginStart: 6dp
- layout_weight: 8

**Title**

- component: TextView
- marginBottom: 2dp
- textSize: 12dp
- textStyle: bold

**Updated At**

- component: TextView
- textSize: 10dp

**Sync Icon**

- component: ImageButton
- layout_height: match_parent
- layout_weight: 1
- background: @android:color/transparent
- minWidth: 48dp
- padding: 2dp
- scaleType: centerInside
- src:
</details>

### 5-2. Header Updated At

Set Updated At

```kotlin
// updateAppWidget in GithubNotificationProvider.kt
views.setTextViewText(
    R.id.widgetUpdatedAt,
    "마지막 업데이트: ${SimpleDateFormat("H:mm").format(Date())}"
)
```
