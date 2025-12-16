<script setup>

</script>

<template>
  <div class="container-fluid list-container">
    <div class="row">

      <div class="mt-3">
        <div class="alert alert-danger" role="alert">
          页面找不到，请跳转到首页
        </div>
      </div>
      <router-link to="/" class="alert-link">
        <div class="mt-3">
          <div class="alert alert-success" role="alert">
            点击跳转
          </div>
        </div>
      </router-link>

    </div>
  </div>
</template>

<style scoped>

</style>