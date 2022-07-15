import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import {Wall} from "./Wall"

//Playground（长方形）上面new一个GameMap（正方形） 所以需要传入parent即Playground让map获取playground的长宽
export class GameMap extends AcGameObject{
    constructor(ctx,parent) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;//一个单位的长度（为了自适应性用相对距离不用绝对距离  地图是13*13的）

        this.rows = 13;
        this.cols = 14;

        this.walls=[];
        this.inner_walls_count=40;
        this.snakes = [
            new Snake({id:0,color:"#4876EC",r:this.rows-2,c:1},this),
            new Snake({id:1,color:"#F94848",r:1,c:this.cols-2},this),
        ];

    }

    
    check_connectivity(g,sx, sy, tx, ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = true;   
        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for (let i = 0; i < 4; i ++ ) {
            let x = sx + dx[i], y = sy + dy[i];
            if (!g[x][y] && this.check_connectivity(g, x, y, tx, ty))
                return true;
        }

        return false;
    }

    create_walls(){
        const g=[];//bool 数组判断有没有墙
        for(let r=0;r<this.rows;r++){
            g[r] = [];
            for(let c = 0;c<this.cols;c++){
                g[r][c]=false;
            }
        }//初始化g数组

        //四周加上墙  变的是g[][]   
        for(let r = 0;r<this.rows;r++){
            g[r][0]=g[r][this.cols-1]=true;
        }
        for(let c=0;c<this.cols;c++){
            g[0][c]=g[this.rows-1][c]=true;
        }

        //随机墙
        for(let i = 0;i<this.inner_walls_count /2 ;i++){
            for(let j=0;j<1000;j++){
                let r=parseInt(Math.random()*this.rows);
                let c=parseInt(Math.random()*this.cols);
                if(g[r][c]||g[this.rows-1-r][this.cols-1-c]) continue;
                if(r==this.rows-2&&c==1 || r==1&&c==this.cols-2)
                    continue;
                g[r][c]=g[this.rows-1-r][this.cols-1-c]=true;
                break;
            }
        }


        const copy_g = JSON.parse(JSON.stringify(g));
        if(!this.check_connectivity(copy_g,this.rows-2,1,1,this.cols-2)) 
            return false;


        //根据g[][]画墙
        for(let r=0;r<this.rows;r++) {
            for(let c=0;c<this.cols;c++) {
                if(g[r][c]) {
                    this.walls.push(new Wall(r,c,this));//new Wall的时候会把这个wall加入AC_GAME_OBJECTS[]
                                                        //step函数会把遍历这个数组的元素，每个元素去执行他自己的update
                }
            }
        }
        
        
        return true;
    }

    add_listening_events() {
        this.ctx.canvas.focus();//聚焦

        const [snake0,snake1] = this.snakes;  //解包？
        this.ctx.canvas.addEventListener("keydown",e =>{ //绑定键盘事件
            if(e.key === 'w') snake0.set_direction(0);
            else if(e.key === 'd') snake0.set_direction(1);
            else if(e.key === 's') snake0.set_direction(2);
            else if(e.key === 'a') snake0.set_direction(3);

            if(e.key === 'ArrowUp') snake1.set_direction(0);
            else if(e.key === 'ArrowRight') snake1.set_direction(1);
            else if(e.key === 'ArrowDown') snake1.set_direction(2);
            else if(e.key === 'ArrowLeft') snake1.set_direction(3);

        })
    }

    start(){
        for(let i=0;i<1000;i++)
            if(this.create_walls()) 
                break;
        this.add_listening_events();
    }


    update_size() {
        this.L =parseInt (Math.min(this.parent.clientWidth/this.cols,this.parent.clientHeight/this.rows));
        //L取整像素数 消除缝隙
        this.ctx.canvas.width = this.L*this.cols;
        this.ctx.canvas.height = this.L*this.rows;
    }

    check_ready() {//判断两条蛇是否已经准备好下一回合了
        for (const snake of this.snakes) {
            if(snake.status !== "idle") return false;
            if(snake.direction === -1) return false;
        }
        return true; 
    }

    check_valid(cell) { //检测目标位置 next_cell 是否合法，蛇尾特判
        for(const wall of this.walls) {
            if(wall.r === cell.r && wall.c === cell.c)
                return false;
        }

        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if(!snake.check_tail_increasing()) {//当蛇尾前进时，蛇尾不判断
                k--;
            }
            for(let i=0;i<k;i++) {
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                    return false;
            }
        }

        return true;
    }

    next_step() {//让两条蛇进入下一回合
        for(const snake of this.snakes) {
            snake.next_step();
        }
    }

    update(){
        this.update_size();
        if(this.check_ready()) {//准备好就直接进入下一回合喽
            this.next_step();
        }
        this.render();
    }
    render(){
        const color_even = "#AAD751";
        const color_odd = "#A2D149";
        for(let r = 0; r < this.rows; r++){
            for (let c = 0; c < this.cols; c++){
                if((r+c)%2==0) {
                    this.ctx.fillStyle =color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c*this.L,r*this.L,this.L,this.L)
            }
        }
        
    }
}
