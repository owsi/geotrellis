package geotrellis.spark.io.hadoop

import com.github.nscala_time.time.Imports._
import geotrellis.raster.Tile
import geotrellis.spark.io._
import geotrellis.spark.io.index._
import geotrellis.spark.testfiles.TestFiles
import geotrellis.spark.{OnlyIfCanRunSpark, RasterRDD, SpaceTimeKey, TestEnvironment}
import org.joda.time.DateTime

abstract class HadoopSpaceTimeSpec
  extends PersistenceSpec[SpaceTimeKey, Tile]
          with OnlyIfCanRunSpark
          with TestEnvironment with TestFiles
          with CoordinateSpaceTimeTests {
  type Container = RasterRDD[SpaceTimeKey]

  lazy val reader = HadoopLayerReader[SpaceTimeKey, Tile, RasterRDD](outputLocal)
  lazy val tiles = HadoopTileReader[SpaceTimeKey, Tile](outputLocal)
  lazy val sample =  CoordinateSpaceTime
}

class HadoopSpaceTimeZCurveByYearSpec extends HadoopSpaceTimeSpec {
  lazy val writer = HadoopLayerWriter[SpaceTimeKey, Tile, RasterRDD](outputLocal, ZCurveKeyIndexMethod.byYear)
}

class HadoopSpaceTimeZCurveByFuncSpec extends HadoopSpaceTimeSpec {
  lazy val writer = HadoopLayerWriter[SpaceTimeKey, Tile, RasterRDD](outputLocal, ZCurveKeyIndexMethod.by{ x =>  if (x < DateTime.now) 1 else 0 })
}

class HadoopSpaceTimeHilbertSpec extends HadoopSpaceTimeSpec {
  lazy val writer = HadoopLayerWriter[SpaceTimeKey, Tile, RasterRDD](outputLocal, HilbertKeyIndexMethod(DateTime.now - 20.years, DateTime.now, 4))
}

class HadoopSpaceTimeHilbertWithResolutionSpec extends HadoopSpaceTimeSpec {
  lazy val writer = HadoopLayerWriter[SpaceTimeKey, Tile, RasterRDD](outputLocal,  HilbertKeyIndexMethod(2))
}
