namespace StampieAppServer.Migrations
{
    using StampieAppServer.Data.Codebooks;
    using StampieAppServer.Data.Entities;
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;

    internal sealed class Configuration : DbMigrationsConfiguration<StampieAppServer.Models.ApplicationDbContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        protected override void Seed(StampieAppServer.Models.ApplicationDbContext context)
        {
            List<Stamp> stamps = new List<Stamp>
            {
                new Stamp { StampId = "S01", Category = "C01", County = "O01", GpsPositionLat = 51, GpsPositionLng = 15.1, Name = "Stamp01",
                    Published = DateTime.Parse("2017-01-01 01:00:00"), Type = StampType.Touristic, SellingPlace1 = "SP01", SellingPlace1Web = "SPW01" },
                new Stamp { StampId = "S02", Category = "C02", County = "O02", GpsPositionLat = 52, GpsPositionLng = 15.2, Name = "Stamp02",
                    Published = DateTime.Parse("2017-01-02 02:00:00"), Type = StampType.Touristic, SellingPlace1 = "SP02", SellingPlace1Web = "SPW02" },
                new Stamp { StampId = "S03", Category = "C03", County = "O03", GpsPositionLat = 53, GpsPositionLng = 15.3, Name = "Stamp03",
                    Published = DateTime.Parse("2017-01-03 03:00:00"), Type = StampType.Touristic, SellingPlace1 = "SP03", SellingPlace1Web = "SPW03" },
                new Stamp { StampId = "S04", Category = "C04", County = "O04", GpsPositionLat = 54, GpsPositionLng = 15.4, Name = "Stamp04",
                    Published = DateTime.Parse("2017-01-04 04:00:00"), Type = StampType.Touristic, SellingPlace1 = "SP04", SellingPlace1Web = "SPW04" },
                new Stamp { StampId = "S05", Category = "C05", County = "O05", GpsPositionLat = 55, GpsPositionLng = 15.5, Name = "Stamp05",
                    Published = DateTime.Parse("2017-01-05 05:00:00"), Type = StampType.Touristic, SellingPlace1 = "SP05", SellingPlace1Web = "SPW05" }
            };
            stamps.ForEach(s => context.Stamps.AddOrUpdate(stamp => stamp.StampId, s));
            context.SaveChanges();
        }
    }
}
