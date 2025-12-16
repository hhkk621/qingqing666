<template>
  <div class="rank">
    <div class="row rank-title">
      <h1>排行榜</h1>
    </div>
    <div class="row rank-main">
      <div class="col-lg-8">
        <div class="row">
          <div class="col-lg-6 rank-charm">
            <h2>魅力榜</h2>
            <ul class="nav nav-pills mb-3" id="charm-tab" role="tablist">
              <li class="nav-item" role="presentation">
                <button class="nav-link active" id="charm-week-tab"
                        data-bs-toggle="pill" data-bs-target="#charm-week"
                        type="button" role="tab" aria-controls="charm-week"
                        aria-selected="true" @click="charmTotal">周榜
                </button>
              </li>
              <li class="nav-item" role="presentation">
                <button class="nav-link" id="charm-total-tab"
                        data-bs-toggle="pill" data-bs-target="#charm-total"
                        type="button" role="tab" aria-controls="charm-total"
                        aria-selected="false" @click="charmTotal">总榜
                </button>
              </li>
            </ul>
            <div class="tab-content" id="pills-tabContent">
              <div class="tab-pane fade show active" id="charm-week" role="tabpanel"
                   aria-labelledby="charm-week-tab" tabindex="0">
                <div class="rank-user">
                  <div class="rank-header"><img src="http://cd.ray-live.cn/imgs/headpic/pic_90.jpg"></div>
                  <div class="user-text"><h5>薄荷星辰2</h5>
                    <h7>魅力值:920</h7>
                  </div>
                </div>
              </div>
              <div class="tab-pane fade" id="charm-total" role="tabpanel"
                   aria-labelledby="charm-total-tab" tabindex="0">
                <div class="rank-user">
                  <div class="rank-header"><img src="http://cd.ray-live.cn/imgs/headpic/pic_9.jpg"></div>
                  <div class="user-text"><h5>薄荷星辰</h5>
                    <h7>魅力值:920</h7>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="col-lg-6 rank-riches">
            <h2>财富榜</h2>
            <ul class="nav nav-pills mb-3" id="riches-tab" role="tablist">
              <li class="nav-item" role="presentation">
                <button class="nav-link active" id="riches-week-tab"
                        data-bs-toggle="pill" data-bs-target="#riches-week"
                        type="button" role="tab" aria-controls="riches-week"
                        aria-selected="true" @click="richTotal">周榜
                </button>
              </li>
              <li class="nav-item" role="presentation">
                <button class="nav-link" id="riches-total-tab"
                        data-bs-toggle="pill" data-bs-target="#riches-total"
                        type="button" role="tab" aria-controls="riches-total"
                        aria-selected="false" @click="richTotal">总榜
                </button>
              </li>
            </ul>
            <div class="tab-content" id="pills-tabContent">
              <div class="tab-pane fade show active" id="riches-week" role="tabpanel"
                   aria-labelledby="riches-week-tab" tabindex="0">
                <div class="rank-user">
                  <div class="rank-header"><img src="http://cd.ray-live.cn/imgs/headpic/pic_20.jpg"></div>
                  <div class="user-text"><h5>梦幻夜曲</h5>
                    <h7>魅力值:970</h7>
                  </div>
                </div>
              </div>
              <div class="tab-pane fade" id="riches-total" role="tabpanel"
                   aria-labelledby="riches-total-tab" tabindex="0">
                <div class="rank-user">
                  <div class="rank-header"><img src="http://cd.ray-live.cn/imgs/headpic/pic_22.jpg"></div>
                  <div class="user-text"><h5>梦幻花海</h5>
                    <h7>魅力值:861</h7>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-lg-4 rank-medal">
        <h2>勋章墙</h2>
        <figure class="figure">
          <img src="http://cd.ray-live.cn/imgs/headpic/pic_13.jpg" class="figure-img img-fluid rounded" alt="...">
          <figcaption class="figure-caption"><h1>秋日冠军</h1></figcaption>
        </figure>
        <figure class="figure">
          <img src="http://cd.ray-live.cn/imgs/headpic/pic_110.jpg" class="figure-img img-fluid rounded" alt="...">
          <figcaption class="figure-caption"><h1>夏日冠军</h1></figcaption>
        </figure>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from "vue";
import router from '@/router/index'

let charmTotal = () => {

}

let richTotal = () => {

}

onMounted(() => {

});
</script>

<style scoped>

</style>