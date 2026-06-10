# 功能演示(幻灯片)

> 像看 PPT 一样过一遍用户中心的全部功能。**← → 方向键**或下方按钮翻页;想亲手玩,见最后一页的演示账号。

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const cur = ref(0)
const total = 7
const next = () => { if (cur.value < total - 1) cur.value++ }
const prev = () => { if (cur.value > 0) cur.value-- }
const go = (i) => { cur.value = i }
const onKey = (e) => {
  if (e.key === 'ArrowRight' || e.key === 'ArrowDown') next()
  if (e.key === 'ArrowLeft' || e.key === 'ArrowUp') prev()
}
onMounted(() => window.addEventListener('keydown', onKey))
onUnmounted(() => window.removeEventListener('keydown', onKey))
</script>

<div class="deck">
  <!-- 幻灯片区域 -->
  <Transition name="slide" mode="out-in">
    <div class="slide" :key="cur">
      <!-- ① 封面 -->
      <template v-if="cur === 0">
        <div class="s-tag">用户中心 · User Center</div>
        <h2 class="s-title">一个能跑的全栈用户系统</h2>
        <p class="s-sub">注册 / 登录 / 用户管理,从需求到 Docker 部署全流程实现</p>
        <div class="chips">
          <span class="chip a1">Spring Boot 3</span>
          <span class="chip a2">React 19</span>
          <span class="chip a3">BCrypt + JWT</span>
          <span class="chip a4">MySQL 8 + Redis</span>
          <span class="chip a5">Docker Compose</span>
        </div>
        <p class="s-hint">按 → 开始</p>
      </template>
      <!-- ② 注册 -->
      <template v-else-if="cur === 1">
        <h2 class="s-title">① 注册</h2>
        <div class="duo">
          <div class="mock a1">
            <div class="mock-bar">用户中心 · 注册</div>
            <div class="mock-field"><span>账号</span><div class="mock-input">demo_user</div></div>
            <div class="mock-field"><span>密码</span><div class="mock-input">••••••••</div></div>
            <div class="mock-field"><span>确认密码</span><div class="mock-input">••••••••</div></div>
            <div class="mock-field"><span>星球编号</span><div class="mock-input">10086</div></div>
            <div class="mock-btn">注 册</div>
          </div>
          <ul class="points">
            <li class="a2">前端即时校验:账号 ≥4 位、密码 ≥8 位、两次一致</li>
            <li class="a3">后端再查一遍 + 唯一性校验(账号 / 星球编号不可重复)</li>
            <li class="a4">密码用 <b>BCrypt</b> 加密入库,数据库里看不到明文</li>
          </ul>
        </div>
      </template>
      <!-- ③ 登录 -->
      <template v-else-if="cur === 2">
        <h2 class="s-title">② 登录(BCrypt + JWT)</h2>
        <div class="flow">
          <div class="fstep a1">输入账号密码</div>
          <div class="farrow a2">→</div>
          <div class="fstep a2">BCrypt 比对密文</div>
          <div class="farrow a3">→</div>
          <div class="fstep a3">签发 JWT 令牌</div>
          <div class="farrow a4">→</div>
          <div class="fstep a4">之后每次请求自动带上</div>
        </div>
        <ul class="points">
          <li class="a3">登录态<b>无状态</b>:服务器不存会话,多台机器都能验,天然可扩展</li>
          <li class="a4">密码错误与账号不存在<b>统一提示</b>「账号或密码错误」,不给攻击者线索</li>
        </ul>
      </template>
      <!-- ④ 首页 -->
      <template v-else-if="cur === 3">
        <h2 class="s-title">③ 首页 · 我的信息</h2>
        <div class="mock wide a1">
          <div class="mock-nav"><b>用户中心</b><span class="mock-tab on">首页</span><span class="mock-tab">用户管理</span><span class="mock-user">root <i class="gold">管理员</i> <i class="btn-s">退出</i></span></div>
          <div class="mock-body">
            <div class="kv a2"><span>账号</span><b>root</b></div>
            <div class="kv a3"><span>星球编号</span><b>1</b></div>
            <div class="kv a4"><span>角色</span><b class="gold">管理员</b></div>
            <div class="kv a5"><span>注册时间</span><b>2026-06-10</b></div>
          </div>
        </div>
        <ul class="points"><li class="a4">右上角显示当前用户;<b>管理员才能看到「用户管理」菜单</b></li></ul>
      </template>
      <!-- ⑤ 用户管理 -->
      <template v-else-if="cur === 4">
        <h2 class="s-title">④ 用户管理(仅管理员)</h2>
        <div class="mock wide a1">
          <div class="mock-search">🔍 按用户名搜索…</div>
          <div class="trow th a2"><span>ID</span><span>账号</span><span>角色</span><span>操作</span></div>
          <div class="trow a3"><span>1</span><span>root</span><span class="gold">管理员</span><span class="del">删除</span></div>
          <div class="trow a4"><span>2</span><span>demo</span><span>普通</span><span class="del">删除</span></div>
          <div class="trow a5"><span>3</span><span>alice</span><span>普通</span><span class="del">删除</span></div>
        </div>
        <ul class="points">
          <li class="a3">按用户名<b>模糊搜索</b> + <b>分页</b>(MyBatis-Plus 分页插件)</li>
          <li class="a4">删除是<b>逻辑删除</b>:数据库只标记 isDelete=1,数据可恢复、可审计</li>
        </ul>
      </template>
      <!-- ⑥ 安全 -->
      <template v-else-if="cur === 5">
        <h2 class="s-title">⑤ 权限双保险</h2>
        <div class="duo">
          <div class="guard a1"><div class="g-icon">🖥️</div><b>前端</b><p>非管理员<b>看不到</b>管理菜单,路由直接不渲染</p><i>体验层</i></div>
          <div class="guard a2"><div class="g-icon">🛡️</div><b>后端</b><p>每个管理接口先 <code>ensureAdmin()</code>,普通用户硬闯返回 <b>40101 无权限</b></p><i>安全层(真正的闸门)</i></div>
        </div>
        <ul class="points"><li class="a3">前端隐藏只是体验,<b>后端鉴权才是安全</b>——拼 URL 也绕不过去</li></ul>
      </template>
      <!-- ⑦ 快速开始 -->
      <template v-else>
        <h2 class="s-title">🚀 自己跑起来</h2>
        <div class="code a1">cd user-center<br>docker compose up -d --build<br><span class="dim"># 浏览器打开 http://localhost</span></div>
        <p class="s-sub a2">内置演示账号,开箱即用:</p>
        <div class="accounts">
          <div class="acct gold-b a3"><b>root</b><span>12345678</span><i>管理员(能进用户管理)</i></div>
          <div class="acct a4"><b>demo</b><span>12345678</span><i>普通用户</i></div>
        </div>
        <p class="s-hint a5">⚠️ 演示账号仅限本地/演示环境,上生产前请删除(见建表脚本注释)</p>
      </template>
    </div>
  </Transition>
  <!-- 控制条 -->
  <div class="ctrl">
    <button class="nav-btn" :disabled="cur===0" @click="prev">← 上一页</button>
    <div class="dots">
      <span v-for="i in total" :key="i" class="dot" :class="{on: cur===i-1}" @click="go(i-1)"></span>
    </div>
    <button class="nav-btn" :disabled="cur===total-1" @click="next">下一页 →</button>
  </div>
</div>

想看这些功能背后的实现?每个功能对应实战篇一章:[认证模块](/project/auth) · [用户管理](/project/user-management) · [前端架构](/project/frontend) · [部署上线](/project/deployment)。

<style scoped>
.deck { border: 1px solid var(--vp-c-divider); border-radius: 12px; overflow: hidden; margin: 16px 0;
  background: linear-gradient(160deg, rgba(108,99,255,.07), rgba(72,207,173,.05)); }
.slide { min-height: 380px; padding: 28px 32px; display: flex; flex-direction: column; justify-content: center; }
.slide-enter-active, .slide-leave-active { transition: opacity .3s ease, transform .3s ease; }
.slide-enter-from { opacity: 0; transform: translateX(24px); }
.slide-leave-to { opacity: 0; transform: translateX(-24px); }
.s-tag { color: var(--vp-c-brand-1); font-weight: 600; letter-spacing: 2px; font-size: 13px; }
.s-title { border: none; margin: 6px 0 4px; font-size: 26px; }
.s-sub { color: var(--vp-c-text-2); margin: 4px 0 14px; }
.s-hint { color: var(--vp-c-text-3); font-size: 13px; margin-top: 16px; }
.chips { display: flex; flex-wrap: wrap; gap: 8px; margin-top: 8px; }
.chip { padding: 4px 12px; border-radius: 20px; font-size: 13px; background: rgba(108,99,255,.14);
  border: 1px solid rgba(108,99,255,.35); }
.points { margin: 14px 0 0; padding-left: 18px; }
.points li { margin: 6px 0; }
.duo { display: flex; gap: 24px; align-items: flex-start; flex-wrap: wrap; }
/* CSS 界面 mock */
.mock { background: var(--vp-c-bg); border: 1px solid var(--vp-c-divider); border-radius: 10px;
  padding: 14px; width: 250px; box-shadow: 0 4px 18px rgba(0,0,0,.18); flex-shrink: 0; }
.mock.wide { width: 100%; max-width: 560px; }
.mock-bar { text-align: center; font-weight: 700; margin-bottom: 10px; }
.mock-field { font-size: 12px; margin: 7px 0; }
.mock-field span { color: var(--vp-c-text-3); display: block; margin-bottom: 2px; }
.mock-input { border: 1px solid var(--vp-c-divider); border-radius: 6px; padding: 5px 8px; background: var(--vp-c-bg-soft); }
.mock-btn { background: var(--vp-c-brand-2, #6c63ff); color: #fff; text-align: center;
  border-radius: 6px; padding: 7px; margin-top: 10px; font-size: 13px; }
.mock-nav { display: flex; align-items: center; gap: 12px; border-bottom: 1px solid var(--vp-c-divider);
  padding-bottom: 8px; margin-bottom: 10px; font-size: 13px; }
.mock-tab { color: var(--vp-c-text-3); } .mock-tab.on { color: var(--vp-c-brand-1); font-weight: 600; }
.mock-user { margin-left: auto; font-size: 12px; }
.mock-user i, .gold { font-style: normal; color: #b8860b; background: rgba(254,215,102,.18);
  border: 1px solid rgba(254,215,102,.5); border-radius: 4px; padding: 1px 6px; font-size: 11px; }
.mock-user .btn-s { color: var(--vp-c-text-2); background: var(--vp-c-bg-soft); border-color: var(--vp-c-divider); }
.mock-body { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; }
.kv { background: var(--vp-c-bg-soft); border-radius: 6px; padding: 8px 10px; font-size: 12px; }
.kv span { color: var(--vp-c-text-3); display: block; }
.mock-search { border: 1px solid var(--vp-c-divider); border-radius: 6px; padding: 6px 10px;
  font-size: 12px; color: var(--vp-c-text-3); width: 200px; margin-bottom: 10px; background: var(--vp-c-bg-soft); }
.trow { display: grid; grid-template-columns: 50px 1fr 90px 60px; gap: 8px; padding: 7px 8px;
  font-size: 12px; border-bottom: 1px solid var(--vp-c-divider); }
.trow.th { font-weight: 700; background: var(--vp-c-bg-soft); border-radius: 6px 6px 0 0; }
.del { color: #e5484d; }
/* 登录流程 */
.flow { display: flex; align-items: center; gap: 10px; flex-wrap: wrap; margin: 10px 0; }
.fstep { background: var(--vp-c-bg); border: 1px solid rgba(108,99,255,.4); border-radius: 8px;
  padding: 10px 14px; font-size: 13px; }
.farrow { color: var(--vp-c-brand-1); font-weight: 700; }
/* 双保险 */
.guard { background: var(--vp-c-bg); border: 1px solid var(--vp-c-divider); border-radius: 10px;
  padding: 16px; width: 240px; }
.guard .g-icon { font-size: 26px; }
.guard p { font-size: 13px; margin: 6px 0; }
.guard i { font-size: 12px; color: var(--vp-c-brand-1); font-style: normal; }
/* 快速开始 */
.code { font-family: var(--vp-font-family-mono); background: #0a0a14; color: #48cfad;
  border-radius: 8px; padding: 14px 18px; font-size: 13px; line-height: 1.9; max-width: 460px; }
.code .dim { color: #777; }
.accounts { display: flex; gap: 14px; margin: 10px 0; flex-wrap: wrap; }
.acct { background: var(--vp-c-bg); border: 1px solid var(--vp-c-divider); border-radius: 10px;
  padding: 12px 18px; display: flex; flex-direction: column; gap: 2px; }
.acct b { font-size: 16px; }
.acct span { font-family: var(--vp-font-family-mono); color: var(--vp-c-text-2); }
.acct i { font-size: 12px; color: var(--vp-c-text-3); font-style: normal; }
.acct.gold-b { border-color: rgba(254,215,102,.6); }
/* 控制条 */
.ctrl { display: flex; align-items: center; justify-content: space-between; padding: 12px 18px;
  border-top: 1px solid var(--vp-c-divider); background: var(--vp-c-bg-soft); }
.nav-btn { border: 1px solid var(--vp-c-divider); background: var(--vp-c-bg); border-radius: 18px;
  padding: 5px 16px; font-size: 13px; cursor: pointer; }
.nav-btn:hover:not(:disabled) { border-color: var(--vp-c-brand-1); color: var(--vp-c-brand-1); }
.nav-btn:disabled { opacity: .35; cursor: default; }
.dots { display: flex; gap: 8px; }
.dot { width: 9px; height: 9px; border-radius: 50%; background: var(--vp-c-divider); cursor: pointer; transition: all .2s; }
.dot.on { background: var(--vp-c-brand-1); transform: scale(1.3); }
/* 入场级联动画:每次翻页重挂载,动画自动重播 */
.a1,.a2,.a3,.a4,.a5 { animation: rise .45s ease both; }
.a2 { animation-delay: .12s; } .a3 { animation-delay: .24s; }
.a4 { animation-delay: .36s; } .a5 { animation-delay: .48s; }
@keyframes rise { from { opacity: 0; transform: translateY(14px); } to { opacity: 1; transform: none; } }
</style>
