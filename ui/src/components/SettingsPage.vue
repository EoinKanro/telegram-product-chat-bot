<template>
  <NavigationBar/>

  <div class="tab">
    <div>
      <ul class="nav nav-tabs" role="tablist">
        <li class="nav-item" role="presentation"><a class="nav-link active" role="tab" data-bs-toggle="tab" @click="loadKeyboards" href="#tab-1">Keyboard</a></li>
        <li class="nav-item" role="presentation"><a class="nav-link" role="tab" data-bs-toggle="tab" href="#tab-2">Answer</a></li>
        <li class="nav-item" role="presentation"><a class="nav-link" role="tab" data-bs-toggle="tab" href="#tab-3">Image</a></li>
      </ul>

      <!--Keyboards-->
      <div class="tab-content">
        <div class="tab-pane active" role="tabpanel" id="tab-1">
          <div class="row">

            <div class="col col-md-3 list-group setting-content">
              <!--Left column with names of keyboards-->
              <button class="btn btn-primary list-group-item list-group-item-action" type="button" @click="clearCurrentKeyboard">
                <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16" class="bi bi-plus-circle-fill" style="font-size: 22px;">
                <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM8.5 4.5a.5.5 0 0 0-1 0v3h-3a.5.5 0 0 0 0 1h3v3a.5.5 0 0 0 1 0v-3h3a.5.5 0 0 0 0-1h-3v-3z"></path>
                </svg>&nbsp;
                New
              </button>
              <div v-for="keyboard in keyboards" :key="keyboard.id">
                <button class="btn btn-primary list-group-item list-group-item-action" type="button" @click="setCurrentKeyboard(keyboard)">
                  {{ keyboard.name }}
                </button>
              </div>
            </div>

            <!--Right column with keyboard settings-->
            <div class="col col-md-9 setting-content" style="background: #ffffff;">
              <div class="row">
                <div class="col">
                  <p>Name:&nbsp;<input type="text" v-model="currentKeyboardName"></p>
                </div>
              </div>

              <div class="row">
                <div class="col">
                  <p>Rows:</p>
                  <div class="keyboard-list-row" v-for="(keyboardRow, keyboardRowIndex) in currentKeyboardRows" :key="keyboardRowIndex">
                    <ul class="keyboard-list-row">
                      <li class="keyboard-list-row-element" v-for="(keyboardRowButton, keyboardRowButtonIndex) in keyboardRow" :key="keyboardRowButtonIndex">
                        {{ keyboardRowButton }}
                        <!--Delete keyboard button and row if empty-->
                        <button class="btn btn-primary delete-button keyboard-row-button" type="button" @click="deleteButtonFromKeyboardAndCheck(keyboardRow, keyboardRowIndex, keyboardRowButtonIndex)">
                          <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16" class="bi bi-x" style="color: rgb(0,0,0);border-style: none;width: 1em;height: 1em;">
                            <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"></path>
                          </svg>
                        </button>
                      </li>

                      <!--Add button to row-->
                      <li class="keyboard-list-row-element">
                        <input type="text" v-model="currentKeyboardRowsNewButtons[keyboardRowIndex]" v-on:keyup.enter="addElementToArray(keyboardRow, currentKeyboardRowsNewButtons[keyboardRowIndex])">
                        <button class="btn" type="button" @click="addElementToArray(keyboardRow, currentKeyboardRowsNewButtons[keyboardRowIndex])">
                          <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16" class="bi bi-plus-circle-fill" style="font-size: 22px;">
                            <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM8.5 4.5a.5.5 0 0 0-1 0v3h-3a.5.5 0 0 0 0 1h3v3a.5.5 0 0 0 1 0v-3h3a.5.5 0 0 0 0-1h-3v-3z"></path>
                          </svg>
                        </button>
                      </li>
                    </ul>
                  </div>
                  <button class="btn btn-primary btn-add-row" type="button" @click="addKeyboardRow">Add row</button>
                  <div>
                    <button class="btn btn-primary btn-save-delete-row" type="button" @click="deleteKeyboard">Delete</button>
                    <button class="btn btn-primary btn-save-delete-row" type="button" @click="saveKeyboard">Save</button>
                  </div>

                </div>
              </div>
            </div>

          </div>
        </div>
        <div class="tab-pane" role="tabpanel" id="tab-2">
          <div class="row">
            <div class="col-md-4"></div>
            <div class="col-md-8"></div>
          </div>
        </div>
        <div class="tab-pane" role="tabpanel" id="tab-3">
          <div class="row">
            <div class="col-md-4"></div>
            <div class="col-md-8"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavigationBar from "@/components/NavigationBar";

export default {
  name: "SettingsPage",
  components: {NavigationBar},
  data() {
    return {
      keyboards: [],
      currentKeyboardId: '',
      currentKeyboardName: '',
      currentKeyboardRows: [],
      currentKeyboardRowsNewButtons: []
    }
  },
  methods: {
    //general
    addElementToArray(array, element) {
      array.push(element)
    },
    deleteElementFromArray(array, index) {
      array.splice(index, 1)
    },
    isArrayEmpty(array) {
      if (!array) {
        return true
      }
      if(Object.prototype.toString.call(array) !== '[object Array]') {
        return false
      }
      if (array.length <= 0) {
        return true
      }
      for (const element of array) {
        if (!this.isArrayEmpty(element)) {
          return false
        }
      }
      return true
    },
    //keyboards
    loadKeyboards() {
      //TODO on load and updates
      let rows = [['button1', 'button2']]
      let keyboard = {id: 1, name: 'Keyboard first', rows:rows}
      this.keyboards = [keyboard]
      console.log('keyboards loaded')
    },
    clearCurrentKeyboard() {
      this.currentKeyboardId = ''
      this.currentKeyboardName = ''
      this.currentKeyboardRows = []
      this.currentKeyboardRowsNewButtons = []
    },
    setCurrentKeyboard(keyboard) {
      this.currentKeyboardId = keyboard.id
      this.currentKeyboardName = keyboard.name
      this.currentKeyboardRows = keyboard.rows
      if (!this.currentKeyboardRows) {
        this.currentKeyboardRows = []
      }
    },
    addKeyboardRow() {
      this.currentKeyboardRows.push([])
    },
    deleteButtonFromKeyboardAndCheck(keyboardRow, keyboardRowIndex, keyboardButtonIndex) {
      this.deleteElementFromArray(keyboardRow, keyboardButtonIndex)
      if (keyboardRow.length <= 0) {
        this.deleteElementFromArray(this.currentKeyboardRows, keyboardRowIndex)
        this.deleteElementFromArray(this.currentKeyboardRowsNewButtons, keyboardRowIndex)
      }
    },
    saveKeyboard() {
      if (!this.currentKeyboardName || this.isArrayEmpty(this.currentKeyboardRows)) {
        alert('You should set all fields')
        return;
      }
      if (!this.checkCurrentKeyboardName()) {
        alert('Keyboard with name ' + this.currentKeyboardName + ' already exists')
        return;
      }

      //TODO clear rows from empty arrays

      //TODO
      if (this.currentKeyboardId && this.getIndexOfKeyboard(this.currentKeyboardId)) {
        //let keyboard = {id: this.currentKeyboardId, name: this.currentKeyboardName, rows: this.currentKeyboardRows}
        //update
      } else {
        //let keyboard = {name: this.currentKeyboardName, rows: this.currentKeyboardRows}
        //create
      }

      //TODO delete
      this.keyboards.push({id: this.keyboards.length, name: this.currentKeyboardName, rows: this.currentKeyboardRows})

      this.clearCurrentKeyboard()
    },
    checkCurrentKeyboardName() {
      for (const keyboard of this.keyboards) {
        if (this.currentKeyboardName === keyboard.name) {
          return keyboard.id === this.currentKeyboardId
        }
      }
      return true
    },
    getIndexOfKeyboard(id) {
      for (let i = 0; i < this.keyboards.length; i++) {
        if (this.keyboards[i].id === id) {
          return i
        }
      }
      return null
    },
    deleteKeyboard() {
      let index = this.getIndexOfKeyboard(this.currentKeyboardId)
      if (index != null) {
        this.deleteElementFromArray(this.keyboards, index)
      } else {
        this.clearCurrentKeyboard()
      }
    }
  }
}
</script>

<style scoped>
.row {
  margin: 0;
}

.tab {
  background: #e8e8e8;
  margin-top: 4vh;
  margin-right: 2vw;
  margin-left: 2vw;
  height: 83vh;
  min-width: 600px;
}

.tab-content {
  min-height: 77vh;
}

.setting-content {
  max-height: 77vh;
  min-height: 77vh;
  overflow: scroll;
  padding-left: 0.5vw;
  padding-top: 1.5vh;
}

.keyboard-list-row {
  display: flex;
  list-style: none;
  padding: 0
}

.keyboard-list-row-element {
  background-color: var(--bs-gray-400);
  margin-right: 0.5vw;
}

.keyboard-row-button {
  padding: 0;
  background: rgba(0,0,0,0);
  border: 1px solid rgb(0,0,0) ;
}

.delete-button {
  display: inline-flex;
  margin-left: 0.5vw;
  margin-right: 0.5vw;
  height: 1em;
  width: 1em;
}

.btn-add-row {
  background: var(--bs-gray);
}

.btn-save-delete-row {
  float: right;
  margin-left: 0.5vw;
}
</style>