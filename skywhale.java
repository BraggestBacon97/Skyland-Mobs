// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class skywhale<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "skywhale"), "main");
	private final ModelPart whole body;
	private final ModelPart head;
	private final ModelPart right_leg_1;
	private final ModelPart right_leg_2;
	private final ModelPart right_leg_3;
	private final ModelPart left_leg_1;
	private final ModelPart left_leg_2;
	private final ModelPart left_leg_3;

	public skywhale(ModelPart root) {
		this.whole body = root.getChild("whole body");
		this.head = this.whole body.getChild("head");
		this.right_leg_1 = this.whole body.getChild("right_leg_1");
		this.right_leg_2 = this.whole body.getChild("right_leg_2");
		this.right_leg_3 = this.whole body.getChild("right_leg_3");
		this.left_leg_1 = this.whole body.getChild("left_leg_1");
		this.left_leg_2 = this.whole body.getChild("left_leg_2");
		this.left_leg_3 = this.whole body.getChild("left_leg_3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition whole body = partdefinition.addOrReplaceChild("whole body", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.3F));

		PartDefinition tail_r1 = whole body.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -5.0F, -6.0F, 2.0F, 7.0F, 8.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.7F, -1.2217F, 0.0F, 0.0F));

		PartDefinition horn_r1 = whole body.addOrReplaceChild("horn_r1", CubeListBuilder.create().texOffs(26, 25).addBox(-1.0F, -4.0F, -6.0F, 2.0F, 4.0F, 7.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 1.0F, -5.3F, -1.2217F, 0.0F, 0.0F));

		PartDefinition outer_balloon_body_r1 = whole body.addOrReplaceChild("outer_balloon_body_r1", CubeListBuilder.create().texOffs(0, 14).addBox(-3.0F, -7.0F, -4.0F, 6.0F, 7.0F, 7.0F, new CubeDeformation(0.2F))
		.texOffs(0, 0).addBox(-3.0F, -7.0F, -4.0F, 6.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -3.3F, -1.2217F, 0.0F, 0.0F));

		PartDefinition head = whole body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(26, 15).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -2.3F));

		PartDefinition right_leg_1 = whole body.addOrReplaceChild("right_leg_1", CubeListBuilder.create().texOffs(0, 28).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(-2.5F, 3.0F, -1.8F));

		PartDefinition right_leg_2 = whole body.addOrReplaceChild("right_leg_2", CubeListBuilder.create().texOffs(12, 28).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(-2.5F, 3.0F, 0.2F));

		PartDefinition right_leg_3 = whole body.addOrReplaceChild("right_leg_3", CubeListBuilder.create().texOffs(16, 28).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(-2.5F, 2.0F, 2.2F));

		PartDefinition left_leg_1 = whole body.addOrReplaceChild("left_leg_1", CubeListBuilder.create().texOffs(4, 28).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(3.0F, 3.0F, -1.8F));

		PartDefinition left_leg_2 = whole body.addOrReplaceChild("left_leg_2", CubeListBuilder.create().texOffs(8, 28).addBox(-1.0F, -1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(3.0F, 3.0F, 0.2F));

		PartDefinition left_leg_3 = whole body.addOrReplaceChild("left_leg_3", CubeListBuilder.create().texOffs(20, 28).addBox(-0.5F, 0.342F, -0.5603F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(2.5F, 2.0F, 2.2F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		whole body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}