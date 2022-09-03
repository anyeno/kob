<template>
    <div class="result-board">
        <div class="result-board-text"  v-if="$store.state.five.loser === 'A' && $store.state.five.a_id == $store.state.user.id">
            Lose
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser === 'B' && $store.state.five.b_id == $store.state.user.id">
            Lose
        </div>
        <div class="result-board-text" v-else>
            Win
        </div>
    </div>
</template>

<script>
    export default{

    }
</script>

<style  scoped>
.result-board{
    height: 35vh;
    width: 30vw;
    background-color: rgba(50,50,50, 0.5);
    position: absolute;
    top: 30vh;
    left: 35vw;
}
.result-board-text{
    text-align: center;
    color: white;
    font-size: 50px;
    font-weight: 550;
    font-style: italic;
    padding-top:5vh;
}
.result-board-btn{
    text-align: center;
    padding-top:7vh;
}
</style>