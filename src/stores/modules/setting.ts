//小仓库；layout组件相关配置仓库
// 引入小仓库
import { defineStore } from 'pinia';
//创建用户小仓库
let useLayOutSettingStore = defineStore('SettingStore', {
    state: () => {
        return {
            fold: false,//用于控制菜单折叠还是收起控制
            refresh: false,//用于控制刷新效果
        }
    }
})

// 对外暴露
export default useLayOutSettingStore;
