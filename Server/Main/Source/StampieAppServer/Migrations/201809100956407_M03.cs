namespace StampieAppServer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class M03 : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.Photos", "Creator_Id", "dbo.Users");
            DropIndex("dbo.Photos", new[] { "Creator_Id" });
            AddColumn("dbo.Photos", "Content", c => c.Binary());
            AlterColumn("dbo.Users", "LastLogin", c => c.DateTime());
            AlterColumn("dbo.Users", "Birthday", c => c.DateTime());
            AlterColumn("dbo.Photos", "Creator_Id", c => c.Guid(nullable: false));
            CreateIndex("dbo.Photos", "Creator_Id");
            AddForeignKey("dbo.Photos", "Creator_Id", "dbo.Users", "Id", cascadeDelete: true);
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Photos", "Creator_Id", "dbo.Users");
            DropIndex("dbo.Photos", new[] { "Creator_Id" });
            AlterColumn("dbo.Photos", "Creator_Id", c => c.Guid());
            AlterColumn("dbo.Users", "Birthday", c => c.DateTime(nullable: false));
            AlterColumn("dbo.Users", "LastLogin", c => c.DateTime(nullable: false));
            DropColumn("dbo.Photos", "Content");
            CreateIndex("dbo.Photos", "Creator_Id");
            AddForeignKey("dbo.Photos", "Creator_Id", "dbo.Users", "Id");
        }
    }
}
