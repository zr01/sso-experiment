<template>
  <div id="hello">
    <h1>{{ msg }}</h1>
    <div>
      <ul>
        <li v-for="user in users">
          {{user.username}} <-> {{user.email}}
        </li>
      </ul>
    </div>

    <div>
      <input type="text" v-model="input_val" placeholder="fill me...">
    </div>

    <div>
      Input Value: <span v-text="input_val"></span>
    </div>

    <div>
      <label v-if="ctr % 2 == 0">{{ctr}}</label>
      <button class="btn btn-primary" v-on:click="ctr++">Clicked {{ctr}} times!</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'hello',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App',
      users: [
        { username: 'aatest', email: 'aatest@test.com' },
        { username: 'abtest', email: 'abtest@test.com' },
        { username: 'actest', email: 'actest@test.com' }
      ],
      input_val: '',
      ctr: 0
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
    list-style-position: inside;
}
a {
  color: #42b983;
}
</style>
