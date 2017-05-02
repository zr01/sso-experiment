<template>
  <div id="login">
    <div class="form-group">
      <label>Username:</label>
      <input type="text" class="form-control" v-model="username" placeholder="Username">
    </div>
    <div class="form-group">
      <label>Password:</label>
      <input type="password" class="form-control" v-model="password" placeholder="Password">
    </div>
  </div>
</template>

<script>
export default{
  name: 'login',
  data() {
    return {
      username: '',
      password: ''
    }
  }
}
</script>

<style>
</style>