<template>
  <ion-app class="ion-align-items-center ion-justify-content-center">
    <ion-button
      color="success"
      v-if="githubToken === ''"
      @click="signInWithGithub"
      >SAVE TOKEN</ion-button
    >
    <ion-button color="danger" v-else @click="signOut">REMOVE TOKEN</ion-button>
  </ion-app>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { IonButton, IonApp, alertController } from "@ionic/vue";
import { FirebaseAuthentication } from "@capacitor-firebase/authentication";
import { Capacitor } from "@capacitor/core";

const email = ref("");
const photoUrl = ref("");
const githubToken = ref("");

onMounted(async () => {
  githubToken.value = (await getIdToken()) || "";
});

const getIdToken = async () => {
  if (Capacitor.isNativePlatform()) {
    return "";
  } else {
    return localStorage.getItem("githubToken");
  }
};

const signInWithGithub = async () => {
  const result = await FirebaseAuthentication.signInWithGithub();
  email.value = result?.user?.email as string;
  photoUrl.value = result?.user?.photoUrl as string;
  saveToken(result?.credential?.accessToken as string);
  presentAlert("success sign in");
  return result.user;
};

const saveToken = async (token: string) => {
  githubToken.value = token;
  if (Capacitor.isNativePlatform()) {
    return;
  } else {
    localStorage.setItem("githubToken", token);
  }
};

const signOut = async () => {
  await FirebaseAuthentication.signOut();
  removeToken();
  presentAlert("success sign out");
};

const removeToken = async () => {
  githubToken.value = "";
  if (Capacitor.isNativePlatform()) {
    return;
  } else {
    localStorage.removeItem("githubToken");
  }
};

const presentAlert = async (message: string) => {
  const alert = await alertController.create({
    header: "INFO",
    message: message,
    buttons: ["OK"],
  });

  await alert.present();
};
</script>
