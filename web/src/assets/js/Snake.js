import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export class Snake extends AcGameObject {
    constructor(info,gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;//传入gamemap主要是为了获得某些函数或参数

        this.cells=[new Cell(info.r,info.c)];//存放蛇的身体 cells[0] 是蛇头  
        this.next_cell = null; //下一步蛇移动的目的地 (移动是通过抛出一个蛇头实现的)

        this.speed = 5;//蛇每秒走5个格子
        this.direction = -1;//-1表示没有指令  0 1 2 3 表示 上 右 下 左
        this.status = "idle";//idle：静止  move：移动  die：死亡

        this.dr = [-1,0,1,0];//行方向偏移量
        this.dc = [0,1,0,-1];//列方向偏移量

        this.step = 0;//回合数
        this.eps = 1e-2;//误差

        this.eye_direction = 0;//蛇眼睛朝向  0 1 2 3  上 右 下 左
        if(this.id === 1) this.eye_direction = 2;
        this.eye_dx = [//蛇眼睛的x方向偏移量   两只眼睛
            [-1,1],
            [1,1],
            [1,-1],
            [-1,-1]
        ];
        this.eye_dy = [//蛇眼睛的y方向偏移量
            [-1,-1],
            [-1,1],
            [1,1],
            [1,-1]
        ];
    }

    set_direction(d) {//设置方向的接口
        this.direction = d;
    }

    check_tail_increasing() {//检测当前回合蛇的长度是否增加
        if(this.step <= 10) return true;
        if(this.setp%3 === 1) return true;
        return false;
    }

    start(){

    }

    next_step() {//将蛇的状态变为走下一步
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r+this.dr[d],this.cells[0].c+this.dc[d]);
        this.eye_direction = d;
        this.direction = -1;
        this.status = "move";
        this.step++;

        const k = this.cells.length;
        for(let i = k; i>0;i--) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i-1]));//深层复制  深拷贝
        }

        // if(!this.gamemap.check_valid(this.next_cell)) { //下一步操作非法则去世
        //     this.status = "die";
        // }
        //交给后端判断
    }

    //render是每一帧根据 x,y,L等信息把某一东西画出来  改变x,y,L等参数就可以把物体画到不同的地方，从而实现移动
    update_move() {
        const dx = this.next_cell.x-this.cells[0].x; //蛇头与目标点x方向的距离
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx*dx, dy*dy);//总距离

        if(distance<this.eps){
            this.cells[0] = this.next_cell;//改变蛇头为抛出去的头，即目的地
            this.next_cell = null;
            this.status = "idle";//停下来

            if(!this.check_tail_increasing())
                this.cells.pop(); // 蛇尾扔掉
        } else {
            const move_distance = this.speed*this.timedelta/1000;//每两帧之间走过的距离
            this.cells[0].x +=move_distance*dx/distance;
            this.cells[0].y +=move_distance*dy/distance;
 
            if(!this.check_tail_increasing()) {
                const k = this.cells.length;    
                const tail = this.cells[k-1],tail_target = this.cells[k-2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x+=move_distance*tail_dx/distance;
                tail.y+=move_distance*tail_dy/distance;
            }
        }
    }   

    update() {
        if(this.status === "move")
            this.update_move();
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;
        
        ctx.fillStyle = this.color;

        if(this.status === "die") {
            ctx.fillStyle = "white";
        }

        for(const cell of this.cells){//of遍历的是值   in是下标
            ctx.beginPath();
            ctx.arc(cell.x*L,cell.y*L,L/2*0.8,0,Math.PI*2);
            ctx.fill();
        }

        for (let i=1;i<this.cells.length;i++) {
            const a =this.cells[i-1], b=this.cells[i];
            if(Math.abs(a.x-b.x)<this.eps && Math.abs(a.y-b.y)<this.eps) //两小球重合
                continue;
            if(Math.abs(a.x-b.x)<this.eps) {  //在竖直方向
                ctx.fillRect((a.x-0.4)*L,Math.min(a.y, b.y)*L,L*0.8,Math.abs(a.y-b.y)*L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x)*L,(a.y-0.4)*L,Math.abs(a.x-b.x)*L,L*0.8);
            }
            
        }
        
        ctx.fillStyle = "black";
        for(let i=0;i<2;i++) {
            const eye_x = (this.cells[0].x+this.eye_dx[this.eye_direction][i]*0.25)*L;
            const eye_y = (this.cells[0].y+this.eye_dy[this.eye_direction][i]*0.25)*L;
            ctx.beginPath();
            ctx.arc(eye_x,eye_y,L*0.05,0,Math.PI*2);
            ctx.fill();
        }
    }   
}