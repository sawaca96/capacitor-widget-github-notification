import { CapacitorConfig } from "@capacitor/cli";

const config: CapacitorConfig = {
  appId: "io.ionic.starter",
  appName: "capacitor-widget-github-notification",
  webDir: "dist",
  bundledWebRuntime: false,
  plugins: {
    FirebaseAuthentication: {
      providers: ["github.com"],
    },
  },
};

export default config;
