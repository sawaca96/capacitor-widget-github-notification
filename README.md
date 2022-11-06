## 1. Create Ionic App & Run Android App

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

## 2. Implementation Gihub Login

```
yarn add @capacitor-firebase/authentication firebase capacitor-secure-storage-plugin
```

[✨ Github SignIn](https://github.com/sawaca96/capacitor-widget-github-notification/commit/82e1679e76ae7a0d2ebb86219dc6d812ca72f05b)

## 3. Create Widget

| 1                                                                                                               | 2                                                                                                               | 3                                                                                                               | 4                                                                                                               | 5                                                                                                               |
| --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| ![image](https://user-images.githubusercontent.com/49309322/193579408-5d6271b7-b877-469f-8aaa-8d7ee6e96a57.png) | ![image](https://user-images.githubusercontent.com/49309322/193579707-4c3ec37b-f255-4ce3-8c61-91d0592b4b95.png) | ![image](https://user-images.githubusercontent.com/49309322/193579859-f4f63cb8-f408-4b92-bfde-d8a45c59974f.png) | ![image](https://user-images.githubusercontent.com/49309322/193581041-37db8d94-a278-4965-8147-d1a16a945626.png) | ![image](https://user-images.githubusercontent.com/49309322/193580216-03a343bd-7070-4228-a6b5-9bff0bfeda76.png) |

[🚚 Rename GithubNotificationProvider](https://github.com/sawaca96/capacitor-widget-github-notification/commit/b17f9cb0469bb6bfea1230b173aa27aa3d0895b5)(optional)

[🔧 Update widget config](https://github.com/sawaca96/capacitor-widget-github-notification/commit/a5956d6f3eb0bd60fd7071e804d12a1bd08b1eba)

## 4. Make Vector Asset

| 1                                                            | 2                                                                                                               | 3                                                                                                               |
| ------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| Download SVG from [Octicons](https://primer.style/octicons/) | ![image](https://user-images.githubusercontent.com/49309322/193585120-b06efe4c-5c04-4e2c-80e0-e28fd207ce06.png) | ![image](https://user-images.githubusercontent.com/49309322/193585552-af109fbd-c24a-4a42-b587-741f58e4f8f3.png) |

## 5. Header

### Layout

```
// layout/github_notification
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

- orientation: vertical
- xmlns:android: http://schemas.android.com/apk/res/android

**Header**

- orientation: horizontal
- height: 40dp
- background: #292929
- paddingVertical: 4dp
- paddingHorizontal: 10dp
- weightSum: 10

**Github Icon**

- layout_height: match_parent
- layout_weight: 1
- padding: 4dp
- scaleType: fitCenter
- src:

**Title And Updated At**

- orientation: vertical
- marginStart: 6dp
- layout_weight: 8

**Title**

- marginBottom: 2dp
- textSize: 12dp
- textStyle: bold

**Updated At**

- component: TextView
- textSize: 10dp

**Sync Icon**

- layout_height: match_parent
- layout_weight: 1
- background: @android:color/transparent
- minWidth: 48dp
- padding: 2dp
- scaleType: centerInside
- src:
</details>

### Implementaion updated at

[GithubNotificationProvider.kt](https://github.com/sawaca96/capacitor-widget-github-notification/commit/bb5bfadf82da82e35a88fd3dd1bcd8da07d0954f#diff-0c67f0b5703e07bb3252bf0244478da2176d7b9dcc58078bb7bbefaa8585f975)

## 6. List View

### Layout

```
// layout/github_notification
LinearLayout(Notification Root)
  ㄴ LinearLayout(Header)
    ㄴ ImageView(Github Icon)
    ㄴ LinearLayout(Title And Updated At)
      ㄴ TextView(Title)
      ㄴ TextView(Updated At)
    ㄴ ImageButton(Sync Icon)
  ㄴ ListView(Notifications)
  ㄴ TextView(Empty)
```

> Create github_notification_item layout !

```
// layout/github_notification_item
LinearLayout(Item Root)
  ㄴ ImageView(Notification Icon)
  ㄴ LinearLayout(Title And Repo)
    ㄴ TextView(Title)
    ㄴ TextView(Repo)
  ㄴ TextView(Updated At)
```

<details>
<summary>
Design Spec
</summary>

**Notifications**

- background: #1c1c1c
- paddingHorizontal: 10dp
- scrollbars: vertical

**Empty**

- background: #1c1c1c
- gravity: center
- textColor: #ffffff
- textSize: 14dp
- textStyle: bold

**Item Root**

- xmlns:android: http://schemas.android.com/apk/res/android
- layout_width: match_parent
- layout_height: 40dp
- orientation: horizontal
- weightSum: 10

**Notification Icon**

- layout_width: 0dp
- layout_height: match_parent
- layout_weight: 1
- padding: 6dp
- scaleType: fitCenter

**Title And Repo**

- layout_width: 0dp
- layout_height: match_parent
- layout_weight: 9
- orientation: vertical
- paddingHorizontal: 10dp
- paddingVertical: 6dp
- weightSum: 20

**Title**

- layout_width: match_parent
- layout_height: 0dp
- layout_gravity: center
- layout_marginBottom: 2dp
- layout_weight: 12
- ellipsize: end
- maxLines: 1
- textColor: #ffffff
- textSize: 10dp
- textStyle: bold

**Repo**

- layout_width: match_parent
- layout_height: 0dp
- layout_gravity: center
- layout_weight: 8
- ellipsize: end
- maxLines: 1
- textSize: 8dp

**Updated At**

- layout_width: wrap_content
- layout_height: wrap_content
- layout_gravity: center|end
- textSize: 8dp

</details>

> 💡 Below contents referred by [Android Docs](https://developer.android.com/guide/topics/appwidgets/index.html#collections)

### Create RemoteViewsSerivce

| 1                                                                                                               | 2                                                                                                                                         |
| --------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| ![image](https://user-images.githubusercontent.com/49309322/198821200-a5433dcc-5590-4dfe-a3dc-6975b5532718.png) | <img width="376" alt="image" src="https://user-images.githubusercontent.com/49309322/198821213-173ed69b-7fb6-41f2-aaef-7476f3c79350.png"> |

```
import android.content.Context
import android.content.Intent
import android.widget.RemoteViewsService

class GithubNotificationService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return GithubNotificationFactory(this.applicationContext, intent)
    }
}

class GithubNotificationFactory(
    private val context: Context,
    intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

    //... include adapter-like methods here. See the StackView Widget sample.

}
```

| 1                                                                                                               | 2                                                                                                                                         | 3                                                                                                                                         |
| --------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| ![image](https://user-images.githubusercontent.com/49309322/198821335-8ecb403c-522c-40c5-a560-309d6312d7ad.png) | <img width="439" alt="image" src="https://user-images.githubusercontent.com/49309322/198821349-f7cfb9d1-6ee9-49f0-9e6f-10193633b4b6.png"> | <img width="541" alt="image" src="https://user-images.githubusercontent.com/49309322/198821401-095273f9-69e7-4bef-b628-f1b4eed6c1eb.png"> |

### Create Notification Class

[Notification.kt](https://github.com/sawaca96/capacitor-widget-github-notification/commit/1142347d95e816061c971c9f4921702456657019#diff-051ce5adc1c2e8ee6166464c8587a3e037e704d61cda34f6e8d355f98268484a)

### Implementation RemoteViewsService

[GithubNotificationService.kt](https://github.com/sawaca96/capacitor-widget-github-notification/commit/1142347d95e816061c971c9f4921702456657019#diff-dd478f0760b2dd124ed034de98a26779dbbc8ca79d307ea8db921b8dd19af6a6)

### Update AndroidManifest

[AndroidManifest.xml](https://github.com/sawaca96/capacitor-widget-github-notification/commit/1142347d95e816061c971c9f4921702456657019#diff-63272c98a6330850888e633b43ba4c9decee6431a115e0d2731564838eaa1d1d)

### Add adapter

[GithubNotificationProvider.kt](https://github.com/sawaca96/capacitor-widget-github-notification/commit/1142347d95e816061c971c9f4921702456657019#diff-0c67f0b5703e07bb3252bf0244478da2176d7b9dcc58078bb7bbefaa8585f975)
