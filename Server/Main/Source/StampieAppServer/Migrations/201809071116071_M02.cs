namespace StampieAppServer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class M02 : DbMigration
    {
        public override void Up()
        {
            DropForeignKey("dbo.Users", "Account_Id", "dbo.Accounts");
            DropIndex("dbo.Users", new[] { "Account_Id" });
            AddColumn("dbo.Users", "IsAnonymous", c => c.Boolean(nullable: false));
            AddColumn("dbo.Users", "Username", c => c.String());
            AddColumn("dbo.Users", "Password", c => c.String());
            AddColumn("dbo.Users", "LastLogin", c => c.DateTime(nullable: false));
            AddColumn("dbo.Users", "Country", c => c.String());
            DropColumn("dbo.Users", "Account_Id");
            DropTable("dbo.Accounts");
        }
        
        public override void Down()
        {
            CreateTable(
                "dbo.Accounts",
                c => new
                    {
                        Id = c.Guid(nullable: false, identity: true),
                        IsAnonymous = c.Boolean(nullable: false),
                        Username = c.String(),
                        Password = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            AddColumn("dbo.Users", "Account_Id", c => c.Guid(nullable: false));
            DropColumn("dbo.Users", "Country");
            DropColumn("dbo.Users", "LastLogin");
            DropColumn("dbo.Users", "Password");
            DropColumn("dbo.Users", "Username");
            DropColumn("dbo.Users", "IsAnonymous");
            CreateIndex("dbo.Users", "Account_Id");
            AddForeignKey("dbo.Users", "Account_Id", "dbo.Accounts", "Id", cascadeDelete: true);
        }
    }
}
