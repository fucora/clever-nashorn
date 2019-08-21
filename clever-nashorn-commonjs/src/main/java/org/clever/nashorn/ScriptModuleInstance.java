package org.clever.nashorn;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Getter;
import org.clever.nashorn.folder.FilesystemFolder;
import org.clever.nashorn.folder.Folder;
import org.clever.nashorn.module.Module;
import org.clever.nashorn.module.cache.MemoryModuleCache;
import org.clever.nashorn.module.cache.ModuleCache;
import org.clever.nashorn.utils.ScriptEngineUtils;

import javax.script.Bindings;
import javax.script.ScriptContext;
import java.io.File;

/**
 * NashornScriptEngine Module 实例
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2019/08/21 11:03 <br/>
 */
public class ScriptModuleInstance {

    /**
     * 脚本资源获取
     */
    @Getter
    private Folder folder;
    /**
     * 模块缓存
     */
    @Getter
    private ModuleCache moduleCache;
    /**
     * NashornScriptEngine
     */
    @Getter
    private NashornScriptEngine engine;

    /**
     * root Module
     */
    @Getter
    private Module rootModule;

    /**
     * @param folder      脚本资源获取实现
     * @param moduleCache 模块缓存实现
     */
    public ScriptModuleInstance(Folder folder, ModuleCache moduleCache) {
        this.folder = folder;
        this.moduleCache = moduleCache;
        // 初始化 root Module
        engine = ScriptEngineUtils.creatEngine();
        Bindings global = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        Bindings module = engine.createBindings();
        ScriptObjectMirror exports = ScriptEngineUtils.newObject();
        rootModule = new Module(engine, moduleCache, folder, module, exports);
        global.put("require", rootModule);
        global.put("module", module);
        global.put("exports", exports);
    }

    /**
     * 创建默认的 ScriptModuleInstance <br />
     * 使用本地文件获取脚本资源<br />
     * 使用内存缓存Module<br />
     *
     * @param rootFilePath 本地文件路径
     */
    public static ScriptModuleInstance creatDefault(String rootFilePath) {
        Folder rootFolder = FilesystemFolder.create(new File(rootFilePath));
        return new ScriptModuleInstance(rootFolder, new MemoryModuleCache());
    }

    /**
     * 使用 require 得到 JS 对象
     */
    public ScriptObjectMirror useJs(String name) {
        return rootModule.useJs(name);
    }
}