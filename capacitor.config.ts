import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.personal.openwebui',
  appName: 'open-webui',
  webDir: 'build', // webDir is still needed for some plugin operations, even if loading remote URL
  server: {
    url: 'https://looksty.com',
    cleartext: false // Use true only if your site is HTTP, not HTTPS
  }
};

export default config;
