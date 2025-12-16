<template>
  <div class="container-fluid banner">
    <div class="row">
      <div id="home_banner" class="carousel slide offset-md-2 col-md-8">
        <div class="carousel-indicators">
          <button type="button" data-bs-target="#home_banner" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
          <button type="button" data-bs-target="#home_banner" data-bs-slide-to="1" aria-label="Slide 2"></button>
          <button type="button" data-bs-target="#home_banner" data-bs-slide-to="2" aria-label="Slide 3"></button>
        </div>
        <div class="carousel-inner">
          <div class="carousel-item active" data-bs-interval="10000">
            <img src="http://cd.ray-live.cn/imgs/jinghua.png" class="d-block w-100" alt="...">
          </div>
          <div class="carousel-item" data-bs-interval="10000">
            <img src="http://cd.ray-live.cn/imgs/qiuyi.png" class="d-block w-100" alt="...">
          </div>
          <div class="carousel-item" data-bs-interval="10000">
            <img src="https://www.javasm.cn/static/upload/image/20240614/1718360362445941.jpg" class="d-block w-100" alt="...">
          </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#home_banner" data-bs-slide="prev">
          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
          <span class="visually-hidden">上一张</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#home_banner" data-bs-slide="next">
          <span class="carousel-control-next-icon" aria-hidden="true"></span>
          <span class="visually-hidden">下一张</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>

</style>