/**
 * navbar.js - 通用导航栏组件 (防错修复版)
 */

class Navbar {
    constructor() {
        this.currentPath = window.location.pathname.split('/').pop() || 'index.html';
        this.init();
    }

    getApiBase() {
        return '/springboot2z04j';
    }

    getMenuData() {
        return [
            { type: 'item', id: 'home.html', icon: '🏠', text: '系统首页' },
            { 
                type: 'group', 
                title: '基础功能', 
                items: [
                    { type: 'item', id: 'profile.html', icon: '👤', text: '个人中心' },
                    { 
                        type: 'parent', 
                        id: 'sys-sub', 
                        icon: '⚙️', 
                        text: '系统管理',
                        children: [
                            { id: 'users.html', text: '用户管理' },
                            { id: 'roles.html', text: '角色管理' }
                        ]
                    }
                ]
            },
            {
                type: 'group',
                title: '业务管理',
                items: [
                    { type: 'item', id: 'employees.html', icon: '👷', text: '设计师信息管理' },
                    { type: 'item', id: 'gongsixinxi.html', icon: '🏢', text: '公司信息管理' },
                    { type: 'item', id: 'category.html', icon: '📦', text: '商品分类管理' },
                    { type: 'item', id: 'product.html', icon: '🛍️', text: '装修商品管理' },
                    { type: 'item', id: 'work.html', icon: '🎨', text: '装修作品管理' },
                    { type: 'item', id: 'order.html', icon: '📝', text: '订单管理' }
                ]
            }
        ];
    }

    injectStyles() {
        if (document.getElementById('navbar-style')) return;
        const style = document.createElement('style');
        style.id = 'navbar-style';
        style.textContent = `
            body { margin: 0; padding: 0; }
            .top-bar { height: 60px; background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%); color: white; display: flex; align-items: center; justify-content: space-between; padding: 0 24px; box-shadow: 0 2px 8px rgba(0,0,0,0.15); position: fixed; top: 0; left: 0; right: 0; z-index: 1000; }
            .top-bar h1 { font-size: 20px; font-weight: 600; margin: 0; }
            .user-info { display: flex; align-items: center; gap: 12px; font-size: 14px; }
            .user-info button { padding: 6px 16px; background: rgba(255,255,255,0.2); border: 1px solid rgba(255,255,255,0.3); color: white; border-radius: 4px; cursor: pointer; transition: all 0.3s; }
            .user-info button:hover { background: rgba(255,255,255,0.3); }
            .main-container { display: flex !important; margin-top: 60px; min-height: calc(100vh - 60px); position: relative; }
            .sidebar { width: 220px; background: #001529; color: white; flex-shrink: 0; overflow-y: auto; height: calc(100vh - 60px); position: fixed; left: 0; top: 60px; z-index: 900; transition: left 0.3s; }
            .content { flex: 1; padding: 24px; overflow-y: auto; margin-left: 220px; width: calc(100% - 220px); transition: margin-left 0.3s; } 
            .menu-item { padding: 16px 24px; cursor: pointer; transition: all 0.3s; border-left: 3px solid transparent; display: flex; align-items: center; gap: 10px; font-size: 14px; color: rgba(255,255,255,0.65); text-decoration: none;}
            .menu-item:hover { background: rgba(255,255,255,0.1); color: white; }
            .menu-item.active { background: #1890ff; border-left-color: #40a9ff; color: white; }
            .menu-item .icon { width: 16px; height: 16px; display: inline-block; }
            .menu-group { margin-top: 8px; }
            .menu-group-title { padding: 16px 24px 8px; font-size: 12px; color: rgba(255,255,255,0.4); text-transform: uppercase; }
            .menu-item.has-submenu { justify-content: space-between; }
            .menu-item .arrow { font-size: 12px; transition: transform 0.3s; margin-left: auto; }
            .menu-item.open .arrow { transform: rotate(180deg); }
            .menu-item.open { color: white; background: rgba(255,255,255,0.05); }
            .submenu-container { background-color: #000c17; overflow: hidden; display: none; }
            .submenu.menu-item { padding-left: 48px !important; font-size: 13px; }
            .submenu.menu-item.active { background: #1890ff; color: white; }
            @media (max-width: 768px) {
                .sidebar { left: -220px; }
                .sidebar.open { left: 0; }
                .content { margin-left: 0; width: 100%; }
            }
        `;
        document.head.appendChild(style);
    }

    renderTopBar() {
        if (document.querySelector('.top-bar')) return;
        const topBar = document.createElement('div');
        topBar.className = 'top-bar';
        let display = '未登录';
        try {
            const cachedRole = localStorage.getItem('role');
            const cachedUser = localStorage.getItem('username');
            if (cachedUser) {
                display = `${cachedRole || '用户'}: ${cachedUser}`;
            }
        } catch(e) {}
        topBar.innerHTML = `
            <h1>装修公司管理系统</h1>
            <div class="user-info">
                <span>${display}</span>
                <button onclick="logout()">退出登录</button>
            </div>
        `;
        document.body.prepend(topBar);
        let token = '';
        try { token = localStorage.getItem('token') || ''; } catch(e) {}
        if (token) {
            fetch(`${this.getApiBase()}/users/session`, { headers: { 'Token': token } })
                .then(res => res.json())
                .then(d => {
                    if (d && d.code === 0 && d.data) {
                        const u = d.data;
                        const el = topBar.querySelector('.user-info span');
                        el.textContent = `${u.role || '用户'}: ${u.username || ''}`;
                        try {
                            localStorage.setItem('role', u.role || '');
                            localStorage.setItem('username', u.username || '');
                        } catch(e) {}
                    }
                })
                .catch(() => {});
        }
    }

    renderSidebar() {
        if (document.querySelector('.sidebar')) return;
        let container = document.querySelector('.main-container');
        
        if (!container) {
            // 如果还没找到 container，尝试查找 content 并自动修复结构
            const content = document.querySelector('.content');
            if (content) {
                container = document.createElement('div');
                container.className = 'main-container';
                content.parentNode.insertBefore(container, content);
                container.appendChild(content);
            } else {
                // 如果连 content 都没有，说明页面可能还没加载完，或者结构严重错误
                console.error("❌ Navbar Error: 无法找到 .main-container 或 .content。");
                return;
            }
        }

        const sidebar = document.createElement('div');
        sidebar.className = 'sidebar';
        
        let html = '';
        const data = this.getMenuData();

        data.forEach(item => {
            if (item.type === 'item') {
                const isActive = this.currentPath === item.id ? 'active' : '';
                html += `<div class="menu-item ${isActive}" onclick="navigate('${item.id}')">
                            <span class="icon">${item.icon}</span><span>${item.text}</span>
                         </div>`;
            } else if (item.type === 'group') {
                html += `<div class="menu-group"><div class="menu-group-title">${item.title}</div>`;
                item.items.forEach(sub => {
                    if (sub.type === 'item') {
                        const isActive = this.currentPath === sub.id ? 'active' : '';
                        html += `<div class="menu-item ${isActive}" onclick="navigate('${sub.id}')">
                                    <span class="icon">${sub.icon}</span><span>${sub.text}</span>
                                 </div>`;
                    } else if (sub.type === 'parent') {
                        const hasActiveChild = sub.children.some(child => child.id === this.currentPath);
                        const isOpenClass = hasActiveChild ? 'open' : '';
                        const displayStyle = hasActiveChild ? 'block' : 'none';
                        html += `
                            <div class="menu-item has-submenu ${isOpenClass}" onclick="toggleSubmenu('${sub.id}')">
                                <span class="icon">${sub.icon}</span><span>${sub.text}</span><span class="arrow">▼</span>
                            </div>
                            <div id="${sub.id}" class="submenu-container" style="display: ${displayStyle};">
                        `;
                        sub.children.forEach(child => {
                            const isChildActive = this.currentPath === child.id ? 'active' : '';
                            html += `<div class="submenu menu-item ${isChildActive}" onclick="navigate('${child.id}')">
                                        <span>${child.text}</span>
                                     </div>`;
                        });
                        html += `</div>`;
                    }
                });
                html += `</div>`;
            }
        });
        sidebar.innerHTML = html;
        container.prepend(sidebar);
    }

    init() {
        // 定义渲染函数
        const runRender = () => {
            this.injectStyles();
            this.renderTopBar();
            this.renderSidebar();
        };

        // 核心修复逻辑：检查 document.body 是否已经存在
        if (document.body) {
            // 如果 body 已经存在，直接运行
            runRender();
        } else {
            // 如果 body 还是 null，则添加监听器等待加载完成
            document.addEventListener('DOMContentLoaded', runRender);
        }
    }
}

window.navigate = (url) => window.location.href = url;
window.logout = () => { if(confirm('确定退出登录吗？')) { try{localStorage.removeItem('token');}catch(e){} window.location.href='index.html'; } };
window.toggleSubmenu = (id) => {
    const el = document.getElementById(id);
    const p = el.previousElementSibling;
    if(el.style.display==='none'){ el.style.display='block'; p.classList.add('open'); }
    else { el.style.display='none'; p.classList.remove('open'); }
};

new Navbar();
