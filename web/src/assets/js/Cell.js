//蛇身的每一格是一个对象 

export class Cell {
    constructor(r,c) {
        this.r = r;
        this.c = c;
        this.x = c + 0.5;
        this.y = r + 0.5;
    }
}