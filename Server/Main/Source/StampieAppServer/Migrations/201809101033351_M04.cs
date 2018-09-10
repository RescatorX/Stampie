namespace StampieAppServer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class M04 : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.Comments", "Game_Id", "dbo.Games");
            DropForeignKey("dbo.Comments", "Photo_Id", "dbo.Photos");
            DropForeignKey("dbo.Comments", "Stamp_Id", "dbo.Stamps");
            DropForeignKey("dbo.Comments", "Statistic_Id", "dbo.Statistics");
            DropIndex("dbo.Comments", new[] { "Game_Id" });
            DropIndex("dbo.Comments", new[] { "Photo_Id" });
            DropIndex("dbo.Comments", new[] { "Stamp_Id" });
            DropIndex("dbo.Comments", new[] { "Statistic_Id" });
            AddColumn("dbo.Comments", "CommentEntity", c => c.Guid(nullable: false));
            DropColumn("dbo.Comments", "Game_Id");
            DropColumn("dbo.Comments", "Photo_Id");
            DropColumn("dbo.Comments", "Stamp_Id");
            DropColumn("dbo.Comments", "Statistic_Id");
        }
        
        public override void Down()
        {
            AddColumn("dbo.Comments", "Statistic_Id", c => c.Guid());
            AddColumn("dbo.Comments", "Stamp_Id", c => c.Guid());
            AddColumn("dbo.Comments", "Photo_Id", c => c.Guid());
            AddColumn("dbo.Comments", "Game_Id", c => c.Guid());
            DropColumn("dbo.Comments", "CommentEntity");
            CreateIndex("dbo.Comments", "Statistic_Id");
            CreateIndex("dbo.Comments", "Stamp_Id");
            CreateIndex("dbo.Comments", "Photo_Id");
            CreateIndex("dbo.Comments", "Game_Id");
            AddForeignKey("dbo.Comments", "Statistic_Id", "dbo.Statistics", "Id");
            AddForeignKey("dbo.Comments", "Stamp_Id", "dbo.Stamps", "Id");
            AddForeignKey("dbo.Comments", "Photo_Id", "dbo.Photos", "Id");
            AddForeignKey("dbo.Comments", "Game_Id", "dbo.Games", "Id");
        }
    }
}
