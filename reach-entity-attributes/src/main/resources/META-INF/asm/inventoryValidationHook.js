var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function initializeCoreMod() {
    return {
        'inventoryValidationHook': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.Container',
                'methodName': 'm_271806_',
                'methodDesc': '(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/player/Player;I)Z'
            },
            'transformer': function (node) {
                var methodInsn = ASMAPI.findFirstMethodCall(node, ASMAPI.MethodType.VIRTUAL, "net/minecraft/core/BlockPos", "getX", "()I");
                if (methodInsn != null) {
                    var label = new LabelNode();
                    var list = new InsnList();
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 4));
                    list.add(new VarInsnNode(Opcodes.ILOAD, 2));
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/jamieswhiteshirt/reachentityattributes/ReachEntityAttributes", "checkWithinActualReach", "(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;I)Z"));
                    list.add(new JumpInsnNode(Opcodes.IFEQ, label));
                    list.add(new InsnNode(Opcodes.ICONST_1));
                    list.add(new InsnNode(Opcodes.IRETURN));
                    list.add(label);
                    node.instructions.insert(methodInsn, list);
                    ASMAPI.log('DEBUG', 'Injected Inventory canPlayerUse attach range check');
                }
                return node;
            }
        }
    }
}