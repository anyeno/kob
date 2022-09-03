import { AcGameObject } from "./AcGameObject";

export class Five extends AcGameObject {
    constructor(canvas, parent, store, socket) {
        super();
        this.canvas = canvas;
        this.ctx = this.canvas.getContext('2d');
        this.parent = parent;
        this.store = store;
        this.socket = socket;
        this.rows = 15;
        this.cols = 15;
        //this.g = new Array(this.rows);
        this.g = this.store.state.five.g;
        this.steps = this.store.state.five.steps;
        this.order = this.store.state.five.order; // sing 表示单步走   double 表示 双不走
    }
    // start_g() {
    //     for (let i = 0; i < this.rows; i++) {
    //         this.g[i] = new Array(this.cols)
    //     }
    // }

    add_listening_events() {
        this.ctx.canvas.focus();//聚焦
        this.ctx.canvas.addEventListener("click", e => {
            let rect = this.canvas.getBoundingClientRect();
            // let x = e.clientX - rect.left * (this.canvas.width / rect.width);
            // let y = e.clientY - rect.top * (this.canvas.height / rect.height);
            let x = e.clientX - rect.left;
            let y = e.clientY - rect.top;
            //console.log("x:" + x + ",y:" + y);
            let single_width = this.canvas.width / this.cols;
            let single_height = this.canvas.height / this.rows;
            let ch_cols = Math.ceil(x / single_width);
            let ch_rows = Math.ceil(y / single_height);
            //console.log("row: " + ch_rows + " , cols: " + ch_cols);
            if (this.g[ch_rows - 1][ch_cols - 1] != 1 && this.g[ch_rows - 1][ch_cols - 1] != 2) {
                if (this.order === "single" && this.steps % 2 === 0) {
                   console.log("没到你的回合");
                }
                else if (this.order == "double" && this.steps % 2 === 1) {
                    console.log("没到你的回合");
                }
                //this.steps += 1;
                // if (this.steps % 2 == 0)
                //     this.g[ch_rows - 1][ch_cols - 1] = 1;
                // else this.g[ch_rows - 1][ch_cols - 1] = 2;
                else {
                    let gn = 1;
                    if (this.steps % 2 == 0) gn = 2;

                    this.socket.send(JSON.stringify({
                        event: "chess",
                        row: ch_rows - 1,
                        col: ch_cols - 1,
                        g_number: gn
                    }))
                    console.log("发送成功")
                }
            }
        })
    }
    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        //L取整像素数 消除缝隙
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }
    start() {
        //this.start_g();
        this.add_listening_events();
    }
    update() {
        this.update_size();
        this.render();
        this.g = this.store.state.five.g;
        this.steps = this.store.state.five.steps;
    }
    render() {
        const color_even = "#AAD751";
        const color_odd = "#A2D149";
        //const color_black = "black";
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {

                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }

                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L)
                if (this.g[r][c] != 1 && this.g[r][c] != 2) continue;
                if (this.g[r][c] === 1) {
                    this.ctx.fillStyle = "white";
                    this.ctx.strokeStyle = "white";
                }
                else if (this.g[r][c] === 2) {
                    this.ctx.fillStyle = "black";   //填充色
                    this.ctx.strokeStyle = "black";  //线条色
                }
                //this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L)
                //this.ctx.arc((c+0.5)*this.L, (r+0.5)*this.L, this.L / 2, 0, 2*Math.PI, true);
                this.ctx.beginPath();//开始绘制
                this.ctx.arc((c + 0.5) * this.L, (r + 0.5) * this.L, this.L * 0.4, 0, 2 * Math.PI);//arc 的意思是“弧”
                this.ctx.fill();
                this.ctx.stroke();
            }
        }

    }
}