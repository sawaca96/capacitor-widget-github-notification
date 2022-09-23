<template>
  <ion-app class="ion-align-items-center ion-justify-content-center">
    <ion-button
      color="success"
      v-if="status === 'SIGN_OUT'"
      @click="signInWithGithub"
      >SAVE TOKEN</ion-button
    >
    <div v-else-if="status === 'SIGN_IN'" style="display: contents">
      <ion-chip>
        <ion-avatar class="ion-justify-content-center">
          <img alt="github user image" :src="photoUrl" />
        </ion-avatar>
        <ion-label>{{ email }}</ion-label>
      </ion-chip>
      <ion-button color="danger" @click="signOut">REMOVE TOKEN</ion-button>
    </div>
    <ion-spinner v-else type="medium"></ion-spinner>
    <ion-button color="primary" @click="showToast()">SHOW TOAST</ion-button>
  </ion-app>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  IonButton,
  IonApp,
  alertController,
  IonAvatar,
  IonSpinner,
  IonChip,
  IonLabel,
} from "@ionic/vue";
import {
  FirebaseAuthentication,
  SignInResult,
} from "@capacitor-firebase/authentication";
import { SecureStoragePlugin } from "capacitor-secure-storage-plugin";

const photoUrl = ref("");
const email = ref("");
const token = ref("");
const status = ref("PROGRESS");

onMounted(async () => {
  initValues();
});

const initValues = async () => {
  await SecureStoragePlugin.get({ key: "githubToken" })
    .then((result) => {
      token.value = result.value;
      status.value = "SIGN_IN";
    })
    .catch(() => {
      status.value = "SIGN_OUT";
    });
  await SecureStoragePlugin.get({ key: "photoUrl" })
    .then((result) => {
      photoUrl.value = result.value;
    })
    .catch(() => {
      return;
    });
  await SecureStoragePlugin.get({ key: "email" })
    .then((result) => {
      email.value = result.value;
    })
    .catch(() => {
      return;
    });
};

const signInWithGithub = async () => {
  const result = await FirebaseAuthentication.signInWithGithub({
    scopes: ["notifications"],
  });
  await presentAlert("success sign in");
  saveUserInfo(result);
  status.value = "SIGN_IN";
  return result.user;
};

const saveUserInfo = async (result: SignInResult) => {
  token.value = result?.credential?.accessToken as string;
  photoUrl.value = result?.user?.photoUrl as string;
  email.value = result?.user?.email as string;
  SecureStoragePlugin.set({
    key: "githubToken",
    value: token.value,
  });
  SecureStoragePlugin.set({
    key: "photoUrl",
    value: photoUrl.value,
  });
  SecureStoragePlugin.set({
    key: "email",
    value: email.value,
  });
};

const signOut = async () => {
  await FirebaseAuthentication.signOut();
  await presentAlert("success sign out");
  removeUserInfo();
  status.value = "SIGN_OUT";
};

const removeUserInfo = async () => {
  token.value = "";
  photoUrl.value = "";
  email.value = "";
  SecureStoragePlugin.remove({ key: "githubToken" });
  SecureStoragePlugin.remove({ key: "photoUrl" });
  SecureStoragePlugin.remove({ key: "email" });
};

const presentAlert = async (message: string) => {
  const alert = await alertController.create({
    header: "INFO",
    message: message,
    buttons: ["OK"],
  });
  await alert.present();
};

const showToast = () => {
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  window.android.showToast("From Webview");
};
</script>
