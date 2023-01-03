<template>
  <div class="btn-container btn-container-first">
    <button class="btn btn-primary btn-first" type="button" @click="$router.push($settingsPath)">Settings</button>
    <button class="btn btn-primary btn-first" type="button" @click="$router.push($dataPath)">Data</button>
  </div>
  <div class="btn-container">
    <button class="btn btn-primary btn-second" type="button" @click="$router.push($utilsPath)">Utils</button>
  </div>
  <div class="style-slash-first"></div>
  <div class="style-slash-second"></div>
</template>

<script>
export default {
  name: "HomePage",

}
</script>

<style scoped>
.btn-container {
  width: 100%;
  height: 50vh;
}

.btn-container-first {
  position: relative;
  display: flex;
}

.btn-first {
  width: 50%;
}

.btn-second {
  width: 100%;
}

.btn {
  border-radius: 0;
  height: 100%;
  position: relative;
  display: block;
  background: rgba(75,75,75,0.56);
  font-size: 6vw;
  padding: 0;
  margin: 0;
  border-style: none;
}

.btn:hover {
  background-color: rgba(75,75,75,0.75);
}

.style-slash-first {
  background: rgba(255,255,255,0.84);
  position: absolute;
  width: 0.5vw;
  height: 35vh;
  z-index: 1000;
  top: 7vh;
  left: 49.75vw;
  border-radius: 8px;
}

.style-slash-second {
  background: rgba(255,255,255,0.84);
  position: absolute;
  width: 75vw;
  height: 0.6vh;
  z-index: 1000;
  top: 49.8vh;
  left: 10vw;
  border-radius: 8px;
}
</style>