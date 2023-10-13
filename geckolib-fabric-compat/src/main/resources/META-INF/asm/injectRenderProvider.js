var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');

function initializeCoreMod() {
    return {
        'injectRenderProvider': {
            'target': {
                'type': 'METHOD',
                'class': 'software.bernie.geckolib.animatable.client.RenderProvider',
                'methodName': 'of',
                'methodDesc': '(Lnet/minecraft/world/item/Item;)Lsoftware/bernie/geckolib/animatable/client/RenderProvider;'
            },
            'transformer': function (node) {
                for (var i = node.instructions.size() - 1; i >= 0; i--) {
                    var insn = node.instructions.get(i);
                    if (insn.getOpcode() === Opcodes.ARETURN) {
                        var end = new LabelNode();
                        var list = ASMAPI.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 0),
                            new MethodInsnNode(Opcodes.INVOKESTATIC, 'dev/su5ed/sinytra/connectorextras/geckolibcompat/GeckolibFabricCompatSetup', 'get', '(Lnet/minecraft/world/level/ItemLike;)Lsoftware/bernie/geckolib/animatable/client/RenderProvider;'),
                            new InsnNode(Opcodes.DUP),
                            new JumpInsnNode(Opcodes.IFNULL, end),
                            new InsnNode(Opcodes.ARETURN),
                            end,
                            new InsnNode(Opcodes.POP)
                        );
                        node.instructions.insertBefore(insn, list);
                        ASMAPI.log('DEBUG', 'Injected default render provider hook');
                        break;
                    }
                }
                return node;
            }
        }
    }
}