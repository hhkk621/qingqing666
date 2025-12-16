<template>
  <Top/>
  <HomeBanner/>
  <div class="container-fluid main-content-wrapper">
    <HomeHot/>
    <HomeRank/>
  </div>
  <Footer/>
</template>
<script setup>
import Top from "@/components/common/Top.vue";
import Footer from "@/components/common/Footer.vue";
import HomeRank from "@/components/home/HomeRank.vue";
import HomeHot from "@/components/home/HomeHot.vue";
import HomeBanner from "@/components/home/HomeBanner.vue";
</script>