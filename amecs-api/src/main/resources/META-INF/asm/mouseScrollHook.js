var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function initializeCoreMod() {
    return {
        'mouseScrollHook': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.MouseHandler',
                'methodName': 'm_91526_',
                'methodDesc': '(JDD)V'
            },
            'transformer': function (node) {
                var mappedMethodName = ASMAPI.mapMethod("m_6050_");
                for(var i = 0; i < node.instructions.size(); i++) {
                    var insn = node.instructions.get(i);
                    if (insn instanceof MethodInsnNode && insn.getOpcode() == Opcodes.INVOKEVIRTUAL && insn.owner == "net/minecraft/client/gui/screens/Screen" && insn.name == mappedMethodName) {
                        var list = ASMAPI.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 0),
                            new InsnNode(Opcodes.DUP_X1),
                            new InsnNode(Opcodes.POP),
                            new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/MouseHandler", "amecs$onMouseScrolledScreen", "(Z)Z", false)
                        );
                        node.instructions.insert(insn, list);
                        ASMAPI.log('DEBUG', 'Injected mouse scroll hook');
                    }
                }
                return node;
            }
        }
    }
}