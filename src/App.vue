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
import { SecureStoragePlugin } from "capacitor-secure-storage-plugin";

const email = ref("");
const photoUrl = ref("");
const githubToken = ref("");

onMounted(async () => {
  githubToken.value = (await getToken()) || "";
});

const getToken = async () => {
  return (await SecureStoragePlugin.get({ key: "githubToken" })).value;
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
  SecureStoragePlugin.set({ key: "githubToken", value: token });
};

const signOut = async () => {
  await FirebaseAuthentication.signOut();
  removeToken();
  presentAlert("success sign out");
};

const removeToken = async () => {
  githubToken.value = "";
  SecureStoragePlugin.remove({ key: "githubToken" });
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
