1. Create Ionic App & Run Android App
```
npm install -g @ionic/cli@6.20.1

ionic start capacitor-widget-github-notification blank --type vue --capacitor

cd capacitor-widget-github-notification

yarn install

ionic cap add android

ionic cap open android

ionic cap sync android

ionic cap run android -l --external
```

2. Implementation Gihub Login

[✨ Github SignIn](https://github.com/sawaca96/capacitor-widget-github-notification/commit/82e1679e76ae7a0d2ebb86219dc6d812ca72f05b)

Continue...