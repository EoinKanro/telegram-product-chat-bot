<template>
  <section class="position-relative py-4 py-xl-5">
    <div class="container">
      <div class="row mb-5">
        <div class="col-md-8 col-xl-6 text-center mx-auto">
          <h2>Telegram Info Chat Bot</h2>
          <p class="w-lg-50"></p>
        </div>
      </div>
      <div class="row d-flex justify-content-center">
        <div class="col-md-6 col-xl-4">
          <div class="card mb-5">
            <div class="card-body d-flex flex-column align-items-center">
              <div class="bs-icon-xl bs-icon-circle bs-icon-primary bs-icon my-4"><svg class="bi bi-person" xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16">
                <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"></path>
              </svg></div>
              <form @submit="savePassword" class="text-center">
                <div class="mb-3"><input v-model="tempLogin" class="form-control" type="text" placeholder="Username" required autofocus/></div>
                <div class="mb-3"><input  v-model="tempPassword" class="form-control" type="password" placeholder="Password" required/></div>
                <div class="mb-3"><button class="btn btn-primary d-block w-100" type="submit">Login</button></div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

</template>

<script>

export default {
  name: "LoginPage",
  data() {
    return {
      tempLogin: '',
      tempPassword: ''
    }
  },
  methods: {
    async checkAuth() {
      if (!sessionStorage.getItem(loginKey) || !sessionStorage.getItem(passwordKey)) {
        return false
      }
      return await this.checkToken()
    },
    savePassword() {
      sessionStorage.setItem(loginKey, this.tempLogin)
      sessionStorage.setItem(passwordKey, btoa(this.tempPassword))
      this.tempLogin = ''
      this.tempPassword = ''
      setTimeout(() => window.location.reload())
    },
    async checkToken() {
      if (!sessionStorage.getItem(tokenKey)) {
        return await this.login()
      } else {
        if (!await this.checkTokenRequest()) {
          return await this.login()
        } else {
          return true
        }
      }
    },
    async login() {
      let url = 'http://' + this.getCurrentHost() + '/api/auth/login'
      let headers = new Headers();
      headers.set("Username", sessionStorage.getItem(loginKey))
      headers.set("Password", sessionStorage.getItem(passwordKey))

      let response = await fetch(url, {method: 'POST', headers: headers})
      let result = false
      if (response.status === 200) {
        sessionStorage.setItem(tokenKey, await response.text())
        result = true
      } else if(response.status === 401) {
        alert("Wrong password or username")
      } else {
        this.alertError()
      }
    },
    async checkTokenRequest() {
      let url = 'http://' + this.getCurrentHost() + '/api/auth/checkToken'

      let response = await fetch(url, {method: 'POST', body: sessionStorage.getItem(tokenKey)})
      if (response.status !== 200) {
        this.alertError()
      }
      return response.status === 200
    },
    getCurrentHost() {
      return window.location.host
    },
    alertError() {
      alert("Something went wrong")
    }
  }
}

const loginKey = 'login'
const passwordKey = 'password'
const tokenKey = 'token'
</script>

<style scoped>
.bs-icon {
  --bs-icon-size: .75rem;
  display: flex;
  flex-shrink: 0;
  justify-content: center;
  align-items: center;
  font-size: var(--bs-icon-size);
  width: calc(var(--bs-icon-size) * 2);
  height: calc(var(--bs-icon-size) * 2);
  color: var(--bs-primary);
}

.bs-icon-xs {
  --bs-icon-size: 1rem;
  width: calc(var(--bs-icon-size) * 1.5);
  height: calc(var(--bs-icon-size) * 1.5);
}

.bs-icon-sm {
  --bs-icon-size: 1rem;
}

.bs-icon-md {
  --bs-icon-size: 1.5rem;
}

.bs-icon-lg {
  --bs-icon-size: 2rem;
}

.bs-icon-xl {
  --bs-icon-size: 2.5rem;
}

.bs-icon.bs-icon-primary {
  color: var(--bs-white);
  background: var(--bs-primary);
}

.bs-icon.bs-icon-primary-light {
  color: var(--bs-primary);
  background: rgba(var(--bs-primary-rgb), .2);
}

.bs-icon.bs-icon-semi-white {
  color: var(--bs-primary);
  background: rgba(255, 255, 255, .5);
}

.bs-icon.bs-icon-rounded {
  border-radius: .5rem;
}

.bs-icon.bs-icon-circle {
  border-radius: 50%;
}

.card-body {
  background: rgba(228,228,228,0.32);
}
</style>