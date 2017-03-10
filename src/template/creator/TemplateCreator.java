package template.creator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;

import template.ui.TemplateDialog;

/**
 * Created by guojunfu on 17/3/8.
 * 模版类
 */
public class TemplateCreator extends AnAction{

    private static Project mProject;
    private static PsiFile mPsiFile;
    private static Editor mEditor;
    private static Document mDocument;

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        mProject = e.getData(PlatformDataKeys.PROJECT);
        mEditor = e.getData(PlatformDataKeys.EDITOR);
        mPsiFile = e.getData(LangDataKeys.PSI_FILE);

        if (mProject == null || mEditor == null || mPsiFile == null)
            return;

        mDocument = mEditor.getDocument();

        TemplateDialog dialog = new TemplateDialog();
        dialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(mProject));
        dialog.setSize(250, 200);
        dialog.setVisible(true);
    }

    @Override
    public void update(AnActionEvent e) {
        mEditor = e.getData(PlatformDataKeys.EDITOR);

        if (mEditor != null) {
            e.getPresentation().setVisible(true);
        } else {
            e.getPresentation().setVisible(false);
        }
    }

    public static void getInstanceTemplate() {
        String className = mPsiFile.getName().replace(".java", "");
        String content = mDocument.getText();

        if (!content.contains(" class ") || className.equals("")) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("\n\n\tprivate static $CLASS$ instance;\n");
        builder.append("\n");
        builder.append("\tprivate $CLASS$ (){}\n\n");
        builder.append("\tpublic static $CLASS$ getInstance() {\n");
        builder.append("     \tif (instance == null) {                    \n");
        builder.append("         \tsynchronized ($CLASS$.class) {\n");
        builder.append("             \tif (instance == null) {    \n");
        builder.append("                 \tinstance = new $CLASS$();\n");
        builder.append("            \t }\n");
        builder.append("        \t }\n");
        builder.append("    \t }\n\n" );
        builder.append("    \t return instance;\n");
        builder.append("\t}\n");

        String instanceTarget =  builder.toString().replace("$CLASS$", className);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mDocument.insertString(mDocument.getTextLength() - 2, instanceTarget);
            }
        };

        WriteCommandAction.runWriteCommandAction(mProject, runnable);
    }

    public static void getHandlerTemplate() {

        String className = mPsiFile.getName().replace(".java", "");
        String content = mDocument.getText();

        if (!content.contains(" class ") || className.equals("")) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("\n\tprivate MyHandler mHandler = new MyHandler($CLASS$.this);\n\n");
        builder.append("  \tprivate static class MyHandler extends Handler {\n\n");
        builder.append("      \tprivate final WeakReference<$CLASS$> mActivityRef;\n\n");
        builder.append( "      \tpublic MyHandler($CLASS$ activity) {\n");
        builder.append("         \tmActivityRef = new WeakReference<>(activity);\n");
        builder.append("      \t}\n\n");
        builder.append("      \t// 子类必须重写此方法,接受数据\n");
        builder.append("      \t@Override\n");
        builder.append("      \tpublic void handleMessage(Message msg) {\n");
        builder.append("         \tfinal $CLASS$ activity = mActivityRef.get();\n\n");
        builder.append("         \tif (activity == null)\n");
        builder.append("             \treturn;\n\n");
        builder.append("     \t}\n");
        builder.append("  \t}\n");

        String handlerTarget = builder.toString().replace("$CLASS$", className);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mDocument.insertString(mDocument.getTextLength() - 2, handlerTarget);
            }
        };

        WriteCommandAction.runWriteCommandAction(mProject, runnable);
    }
}
