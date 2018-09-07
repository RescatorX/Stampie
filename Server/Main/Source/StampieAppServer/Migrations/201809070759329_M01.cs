namespace StampieAppServer.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class M01 : DbMigration
    {
        public override void Up()
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
            
            CreateTable(
                "dbo.Users",
                c => new
                    {
                        Id = c.Guid(nullable: false, identity: true),
                        Firstname = c.String(),
                        Lastname = c.String(),
                        Birthday = c.DateTime(nullable: false),
                        Account_Id = c.Guid(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Accounts", t => t.Account_Id, cascadeDelete: true)
                .Index(t => t.Account_Id);
            
            CreateTable(
                "dbo.Comments",
                c => new
                    {
                        Id = c.Guid(nullable: false, identity: true),
                        Type = c.Int(nullable: false),
                        Text = c.String(nullable: false),
                        Creator_Id = c.Guid(nullable: false),
                        Game_Id = c.Guid(),
                        Parent_Id = c.Guid(),
                        Photo_Id = c.Guid(),
                        Stamp_Id = c.Guid(),
                        Statistic_Id = c.Guid(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Users", t => t.Creator_Id, cascadeDelete: true)
                .ForeignKey("dbo.Games", t => t.Game_Id)
                .ForeignKey("dbo.Comments", t => t.Parent_Id)
                .ForeignKey("dbo.Photos", t => t.Photo_Id)
                .ForeignKey("dbo.Stamps", t => t.Stamp_Id)
                .ForeignKey("dbo.Statistics", t => t.Statistic_Id)
                .Index(t => t.Creator_Id)
                .Index(t => t.Game_Id)
                .Index(t => t.Parent_Id)
                .Index(t => t.Photo_Id)
                .Index(t => t.Stamp_Id)
                .Index(t => t.Statistic_Id);
            
            CreateTable(
                "dbo.Games",
                c => new
                    {
                        Id = c.Guid(nullable: false, identity: true),
                        Name = c.String(nullable: false),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Photos",
                c => new
                    {
                        Id = c.Guid(nullable: false, identity: true),
                        Description = c.String(),
                        GpsPositionLat = c.Double(nullable: false),
                        GpsPositionLng = c.Double(nullable: false),
                        Creator_Id = c.Guid(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Users", t => t.Creator_Id)
                .Index(t => t.Creator_Id);
            
            CreateTable(
                "dbo.Stamps",
                c => new
                    {
                        Id = c.Guid(nullable: false, identity: true),
                        Type = c.Int(nullable: false),
                        StampId = c.String(nullable: false),
                        Name = c.String(nullable: false),
                        Category = c.String(nullable: false),
                        County = c.String(nullable: false),
                        Published = c.DateTime(nullable: false),
                        SellingPlace1 = c.String(),
                        SellingPlace1Web = c.String(),
                        SellingPlace2 = c.String(),
                        SellingPlace2Web = c.String(),
                        SellingPlace3 = c.String(),
                        SellingPlace3Web = c.String(),
                        SellingPlace4 = c.String(),
                        SellingPlace4Web = c.String(),
                        SellingPlace5 = c.String(),
                        SellingPlace5Web = c.String(),
                        SellingPlace6 = c.String(),
                        SellingPlace6Web = c.String(),
                        SellingPlace7 = c.String(),
                        SellingPlace7Web = c.String(),
                        SellingPlace8 = c.String(),
                        SellingPlace8Web = c.String(),
                        SellingPlace9 = c.String(),
                        SellingPlace9Web = c.String(),
                        SellingPlace10 = c.String(),
                        SellingPlace10Web = c.String(),
                        SellingPlace11 = c.String(),
                        SellingPlace11Web = c.String(),
                        GpsPositionLat = c.Double(nullable: false),
                        GpsPositionLng = c.Double(nullable: false),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Statistics",
                c => new
                    {
                        Id = c.Guid(nullable: false, identity: true),
                        User_Id = c.Guid(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Users", t => t.User_Id, cascadeDelete: true)
                .Index(t => t.User_Id);
            
            CreateTable(
                "dbo.AspNetRoles",
                c => new
                    {
                        Id = c.String(nullable: false, maxLength: 128),
                        Name = c.String(nullable: false, maxLength: 256),
                    })
                .PrimaryKey(t => t.Id)
                .Index(t => t.Name, unique: true, name: "RoleNameIndex");
            
            CreateTable(
                "dbo.AspNetUserRoles",
                c => new
                    {
                        UserId = c.String(nullable: false, maxLength: 128),
                        RoleId = c.String(nullable: false, maxLength: 128),
                    })
                .PrimaryKey(t => new { t.UserId, t.RoleId })
                .ForeignKey("dbo.AspNetRoles", t => t.RoleId, cascadeDelete: true)
                .ForeignKey("dbo.AspNetUsers", t => t.UserId, cascadeDelete: true)
                .Index(t => t.UserId)
                .Index(t => t.RoleId);
            
            CreateTable(
                "dbo.UserGames",
                c => new
                    {
                        Id = c.Guid(nullable: false, identity: true),
                        Score = c.Double(nullable: false),
                        Played = c.DateTime(nullable: false),
                        Game_Id = c.Guid(nullable: false),
                        User_Id = c.Guid(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Games", t => t.Game_Id, cascadeDelete: true)
                .ForeignKey("dbo.Users", t => t.User_Id, cascadeDelete: true)
                .Index(t => t.Game_Id)
                .Index(t => t.User_Id);
            
            CreateTable(
                "dbo.AspNetUsers",
                c => new
                    {
                        Id = c.String(nullable: false, maxLength: 128),
                        Email = c.String(maxLength: 256),
                        EmailConfirmed = c.Boolean(nullable: false),
                        PasswordHash = c.String(),
                        SecurityStamp = c.String(),
                        PhoneNumber = c.String(),
                        PhoneNumberConfirmed = c.Boolean(nullable: false),
                        TwoFactorEnabled = c.Boolean(nullable: false),
                        LockoutEndDateUtc = c.DateTime(),
                        LockoutEnabled = c.Boolean(nullable: false),
                        AccessFailedCount = c.Int(nullable: false),
                        UserName = c.String(nullable: false, maxLength: 256),
                    })
                .PrimaryKey(t => t.Id)
                .Index(t => t.UserName, unique: true, name: "UserNameIndex");
            
            CreateTable(
                "dbo.AspNetUserClaims",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        UserId = c.String(nullable: false, maxLength: 128),
                        ClaimType = c.String(),
                        ClaimValue = c.String(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.AspNetUsers", t => t.UserId, cascadeDelete: true)
                .Index(t => t.UserId);
            
            CreateTable(
                "dbo.AspNetUserLogins",
                c => new
                    {
                        LoginProvider = c.String(nullable: false, maxLength: 128),
                        ProviderKey = c.String(nullable: false, maxLength: 128),
                        UserId = c.String(nullable: false, maxLength: 128),
                    })
                .PrimaryKey(t => new { t.LoginProvider, t.ProviderKey, t.UserId })
                .ForeignKey("dbo.AspNetUsers", t => t.UserId, cascadeDelete: true)
                .Index(t => t.UserId);
            
            CreateTable(
                "dbo.UserStamps",
                c => new
                    {
                        Id = c.Guid(nullable: false, identity: true),
                        Achieved = c.DateTime(nullable: false),
                        Stamp_Id = c.Guid(nullable: false),
                        User_Id = c.Guid(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Stamps", t => t.Stamp_Id, cascadeDelete: true)
                .ForeignKey("dbo.Users", t => t.User_Id, cascadeDelete: true)
                .Index(t => t.Stamp_Id)
                .Index(t => t.User_Id);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.UserStamps", "User_Id", "dbo.Users");
            DropForeignKey("dbo.UserStamps", "Stamp_Id", "dbo.Stamps");
            DropForeignKey("dbo.AspNetUserRoles", "UserId", "dbo.AspNetUsers");
            DropForeignKey("dbo.AspNetUserLogins", "UserId", "dbo.AspNetUsers");
            DropForeignKey("dbo.AspNetUserClaims", "UserId", "dbo.AspNetUsers");
            DropForeignKey("dbo.UserGames", "User_Id", "dbo.Users");
            DropForeignKey("dbo.UserGames", "Game_Id", "dbo.Games");
            DropForeignKey("dbo.AspNetUserRoles", "RoleId", "dbo.AspNetRoles");
            DropForeignKey("dbo.Comments", "Statistic_Id", "dbo.Statistics");
            DropForeignKey("dbo.Statistics", "User_Id", "dbo.Users");
            DropForeignKey("dbo.Comments", "Stamp_Id", "dbo.Stamps");
            DropForeignKey("dbo.Comments", "Photo_Id", "dbo.Photos");
            DropForeignKey("dbo.Photos", "Creator_Id", "dbo.Users");
            DropForeignKey("dbo.Comments", "Parent_Id", "dbo.Comments");
            DropForeignKey("dbo.Comments", "Game_Id", "dbo.Games");
            DropForeignKey("dbo.Comments", "Creator_Id", "dbo.Users");
            DropForeignKey("dbo.Users", "Account_Id", "dbo.Accounts");
            DropIndex("dbo.UserStamps", new[] { "User_Id" });
            DropIndex("dbo.UserStamps", new[] { "Stamp_Id" });
            DropIndex("dbo.AspNetUserLogins", new[] { "UserId" });
            DropIndex("dbo.AspNetUserClaims", new[] { "UserId" });
            DropIndex("dbo.AspNetUsers", "UserNameIndex");
            DropIndex("dbo.UserGames", new[] { "User_Id" });
            DropIndex("dbo.UserGames", new[] { "Game_Id" });
            DropIndex("dbo.AspNetUserRoles", new[] { "RoleId" });
            DropIndex("dbo.AspNetUserRoles", new[] { "UserId" });
            DropIndex("dbo.AspNetRoles", "RoleNameIndex");
            DropIndex("dbo.Statistics", new[] { "User_Id" });
            DropIndex("dbo.Photos", new[] { "Creator_Id" });
            DropIndex("dbo.Comments", new[] { "Statistic_Id" });
            DropIndex("dbo.Comments", new[] { "Stamp_Id" });
            DropIndex("dbo.Comments", new[] { "Photo_Id" });
            DropIndex("dbo.Comments", new[] { "Parent_Id" });
            DropIndex("dbo.Comments", new[] { "Game_Id" });
            DropIndex("dbo.Comments", new[] { "Creator_Id" });
            DropIndex("dbo.Users", new[] { "Account_Id" });
            DropTable("dbo.UserStamps");
            DropTable("dbo.AspNetUserLogins");
            DropTable("dbo.AspNetUserClaims");
            DropTable("dbo.AspNetUsers");
            DropTable("dbo.UserGames");
            DropTable("dbo.AspNetUserRoles");
            DropTable("dbo.AspNetRoles");
            DropTable("dbo.Statistics");
            DropTable("dbo.Stamps");
            DropTable("dbo.Photos");
            DropTable("dbo.Games");
            DropTable("dbo.Comments");
            DropTable("dbo.Users");
            DropTable("dbo.Accounts");
        }
    }
}
