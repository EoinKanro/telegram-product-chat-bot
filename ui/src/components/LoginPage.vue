<template>
  <div class="container">
    <div class="card card-container">
      <img id="profile-img" class="profile-img-card" :src="avatarPhoto" />
      <p id="profile-name" class="profile-name-card"></p>
      <form @submit="savePassword" class="form-signin">
        <input v-model="tempLogin" type="text" id="inputEmail" class="form-control" placeholder="Login" required autofocus>
        <input v-model="tempPassword" type="password" id="inputPassword" class="form-control" placeholder="Password" required>
        <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit">Sign in</button>
      </form>
    </div>
  </div>

</template>

<script>
import avatarPhoto from "../assets/images/avatar.png"

export default {
  name: "LoginPage",
  data() {
    return {
      tempLogin: '',
      tempPassword: '',
      bodyBackgroundClass: 'login-body',
      avatarPhoto
    }
  },
  methods: {
    checkAuth() {
      if (!sessionStorage.getItem(loginKey) || !sessionStorage.getItem(passwordKey)) {
        return false
      }
       return checkToken()
    },
    savePassword() {
      sessionStorage.setItem(loginKey, this.tempLogin)
      sessionStorage.setItem(passwordKey, this.tempPassword)
      this.tempLogin = ''
      this.tempPassword = ''
    }
  }
}

const loginKey = 'login'
const passwordKey = 'password'

function checkToken() {
  return true
}

</script>


<style scoped>

.card-container.card {
  max-width: 350px;
  padding: 40px 40px;
}

.btn {
  font-weight: 700;
  height: 36px;
  -moz-user-select: none;
  -webkit-user-select: none;
  user-select: none;
  cursor: default;
}

/*
 * Card component
 */
.card {
  background-color: rgba(63, 58, 58, 0.65);
  padding: 20px 25px 30px;
  margin: 0 auto 25px;
  margin-top: 50px;
  border-width: 0px;
}

.profile-img-card {
  width: 96px;
  height: 96px;
  margin: 0 auto 10px;
  display: block;
  -moz-border-radius: 50%;
  -webkit-border-radius: 50%;
  border-radius: 50%;
}

/*
 * Form styles
 */
.profile-name-card {
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  margin: 10px 0 0;
  min-height: 1em;
}

.form-signin #inputEmail,
.form-signin #inputPassword {
  direction: ltr;
  height: 44px;
  font-size: 16px;
}

.form-signin input[type=password],
.form-signin input[type=text],
.form-signin button {
  width: 100%;
  display: block;
  margin-bottom: 10px;
  z-index: 1;
  position: relative;
  -moz-box-sizing: border-box;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}

.form-signin .form-control:focus {
  border-color: rgb(104, 145, 162);
  outline: 0;
  -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgb(104, 145, 162);
  box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgb(104, 145, 162);
}

.btn.btn-signin {
  background-color: rgba(47, 42, 42, 0.6);
  padding: 0px;
  font-weight: 700;
  font-size: 14px;
  height: 36px;
  -moz-border-radius: 3px;
  -webkit-border-radius: 3px;
  border-radius: 3px;
  border: none;
  -o-transition: all 0.218s;
  -moz-transition: all 0.218s;
  -webkit-transition: all 0.218s;
  transition: all 0.218s;
}

.btn.btn-signin:hover,
.btn.btn-signin:active,
.btn.btn-signin:focus {
  background-color: rgba(76, 96, 68, 0.6)
}
</style>