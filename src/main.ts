import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

import { IonicVue } from "@ionic/vue";

/* Core CSS required for Ionic components to work properly */
import "@ionic/vue/css/core.css";

/* Basic CSS for apps built with Ionic */
import "@ionic/vue/css/normalize.css";
import "@ionic/vue/css/structure.css";
import "@ionic/vue/css/typography.css";

/* Optional CSS utils that can be commented out */
import "@ionic/vue/css/padding.css";
import "@ionic/vue/css/float-elements.css";
import "@ionic/vue/css/text-alignment.css";
import "@ionic/vue/css/text-transformation.css";
import "@ionic/vue/css/flex-utils.css";
import "@ionic/vue/css/display.css";

/* Theme variables */
import "./theme/variables.css";

/* Firebase */
import { initializeApp } from "firebase/app";
const firebaseConfig = {
  apiKey: "AIzaSyAGyH0UDO9Vy1WX8ZwrS0LNr-DmDywSPmA",
  authDomain: "github-widget-75266.firebaseapp.com",
  projectId: "github-widget-75266",
  storageBucket: "github-widget-75266.appspot.com",
  messagingSenderId: "523922724548",
  appId: "1:523922724548:web:0e2e0e39469beab0d45b91",
};
initializeApp(firebaseConfig);

const app = createApp(App).use(IonicVue).use(router);

router.isReady().then(() => {
  app.mount("#app");
});
