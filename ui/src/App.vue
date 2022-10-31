<template>
  <LoginPage v-if="!isLogged"/>
  <router-view v-if="isLogged"></router-view>
</template>

<script>
import LoginPage from "@/components/LoginPage";

export default {
  name: 'App',
  components: {LoginPage},
  data() {
    return {
      isLogged: false
    }
  },
  beforeMount() {
    (async () => {
      try {
        this.isLogged = await LoginPage.methods.checkAuth()
      } catch (error) {
        console.log('[Exception]', error);
      }
    })();
  }
}
</script>

<style>

</style>
