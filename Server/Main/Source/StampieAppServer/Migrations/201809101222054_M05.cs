namespace StampieAppServer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class M05 : DbMigration
    {
        public override void Up()
        {
            AlterColumn("dbo.Photos", "Content", c => c.String());
            AlterColumn("dbo.Photos", "GpsPositionLat", c => c.Double());
            AlterColumn("dbo.Photos", "GpsPositionLng", c => c.Double());
        }
        
        public override void Down()
        {
            AlterColumn("dbo.Photos", "GpsPositionLng", c => c.Double(nullable: false));
            AlterColumn("dbo.Photos", "GpsPositionLat", c => c.Double(nullable: false));
            AlterColumn("dbo.Photos", "Content", c => c.Binary());
        }
    }
}
